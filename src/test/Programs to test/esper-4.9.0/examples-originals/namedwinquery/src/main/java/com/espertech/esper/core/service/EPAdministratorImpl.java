/**************************************************************************************
 * Copyright (C) 2008 EsperTech, Inc. All rights reserved.                            *
 * http://esper.codehaus.org                                                          *
 * http://www.espertech.com                                                           *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the GPL license       *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package com.espertech.esper.core.service;

import com.espertech.esper.client.*;
import com.espertech.esper.client.context.EPContextPartitionAdmin;
import com.espertech.esper.client.deploy.EPDeploymentAdmin;
import com.espertech.esper.client.soda.*;
import com.espertech.esper.core.deploy.EPDeploymentAdminImpl;
import com.espertech.esper.epl.expression.ExprNode;
import com.espertech.esper.epl.spec.*;
import com.espertech.esper.pattern.EvalFactoryNode;
import com.espertech.esper.util.JavaClassHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for the admin interface.
 */
public class EPAdministratorImpl implements EPAdministratorSPI
{
	private static boolean initialize = false;
	private static List<String> queries = new ArrayList<String>();
	private static int queryposition = 0;

    private final static String SUBS_PARAM_INVALID_USE = "Invalid use of substitution parameters marked by '?' in statement, use the prepare method to prepare statements with substitution parameters";

    private EPServicesContext services;
    private ConfigurationOperations configurationOperations;
    private SelectClauseStreamSelectorEnum defaultStreamSelector;
    private EPDeploymentAdmin deploymentAdminService;

    /**
     * Constructor - takes the services context as argument.
     * @param adminContext - administrative context
     */
    public EPAdministratorImpl(EPAdministratorContext adminContext)
    {
        this.services = adminContext.getServices();
        this.configurationOperations = adminContext.getConfigurationOperations();
        this.defaultStreamSelector = adminContext.getDefaultStreamSelector();

        ConfigurationEngineDefaults.AlternativeContext alternativeContext = adminContext.getServices().getConfigSnapshot().getEngineDefaults().getAlternativeContext();
        StatementIdGenerator statementIdGenerator = null;
        if (alternativeContext != null && alternativeContext.getStatementIdGeneratorFactory() != null) {
            StatementIdGeneratorFactory statementIdGeneratorFactory = (StatementIdGeneratorFactory) JavaClassHelper.instantiate(StatementIdGeneratorFactory.class, alternativeContext.getStatementIdGeneratorFactory());
            statementIdGenerator = statementIdGeneratorFactory.create(new StatementIdGeneratorFactoryContext(services.getEngineURI()));
        }
        this.deploymentAdminService = new EPDeploymentAdminImpl(this, adminContext.getServices().getDeploymentStateService(), adminContext.getServices().getStatementEventTypeRefService(), adminContext.getServices().getEventAdapterService(), adminContext.getServices().getStatementIsolationService(), statementIdGenerator, adminContext.getServices().getFilterService());
    }

    public EPDeploymentAdmin getDeploymentAdmin()
    {
        return deploymentAdminService;
    }

    public EPStatement createPattern(String onExpression) throws EPException
    {
        return createPatternStmt(onExpression, null, null, null);
    }

    public EPStatement createEPL(String eplStatement) throws EPException
    {
        return createEPLStmt(eplStatement, null, null, null);
    }

    public EPStatement createPattern(String expression, String statementName) throws EPException
    {
        return createPatternStmt(expression, statementName, null, null);
    }

    public EPStatement createPattern(String expression, String statementName, Object userObject) throws EPException
    {
        return createPatternStmt(expression, statementName, userObject, null);
    }

    public EPStatement createEPL(String eplStatement, String statementName) throws EPException
    {
        return createEPLStmt(eplStatement, statementName, null, null);
    }

    public EPStatement createEPLStatementId(String eplStatement, String statementName, Object userObject, String statementId) throws EPException
    {
        return createEPLStmt(eplStatement, statementName, userObject, statementId);
    }

    public EPStatement createEPL(String eplStatement, String statementName, Object userObject) throws EPException
    {
        return createEPLStmt(eplStatement, statementName, userObject, null);
    }

    public EPStatement createPattern(String expression, Object userObject) throws EPException
    {
        return createPatternStmt(expression, null, userObject, null);
    }

    public EPStatement createPatternStatementId(String pattern, String statementName, Object userObject, String statementId) throws EPException {
        return createPatternStmt(pattern, statementName, userObject, statementId);
    }

    public EPStatement createEPL(String eplStatement, Object userObject) throws EPException
    {
        return createEPLStmt(eplStatement, null, userObject, null);
    }

    private EPStatement createPatternStmt(String expression, String statementName, Object userObject, String statementId) throws EPException
    {
        StatementSpecRaw rawPattern = EPAdministratorHelper.compilePattern(expression, expression, true, services, SelectClauseStreamSelectorEnum.ISTREAM_ONLY);
        return services.getStatementLifecycleSvc().createAndStart(rawPattern, expression, true, statementName, userObject, null, statementId, null);
    }

    private EPStatement createEPLStmt(String eplStatement, String statementName, Object userObject, String statementId) throws EPException
    {        
	String pathEPLqueries = "./queries.epl";
	File EPLFolder = new File(pathEPLqueries);

	if (!EPLFolder.exists()){
		StatementSpecRaw statementSpec = EPAdministratorHelper.compileEPL(eplStatement, eplStatement, true, statementName, services, defaultStreamSelector);
        	EPStatement statement = services.getStatementLifecycleSvc().createAndStart(statementSpec, eplStatement, false, statementName, userObject, null, statementId, null);
		String pathFile = "/home/lo/EPLcapture/queries.epl";

		try {
			FileWriter eplFile = new FileWriter(pathFile, true);
			BufferedWriter bw = new BufferedWriter(eplFile);
			bw.write(eplStatement + "\n");
			bw.close();
		} catch(IOException e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		log.debug(".createEPLStmt Statement created and started");
	        return statement;
	} else{
		if (!initialize){
			String querybf = "";
			try{
				FileReader ReaderELP = new FileReader(EPLFolder);
				BufferedReader bf = new BufferedReader(ReaderELP);

				while ((querybf = bf.readLine()) != null){
					queries.add(querybf);
				}
				initialize = true;
				bf.close();
			} catch(IOException e){
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		} 
		StatementSpecRaw statementSpec = EPAdministratorHelper.compileEPL(queries.get(queryposition), queries.get(queryposition), true, statementName, services, defaultStreamSelector);
        	EPStatement statement = services.getStatementLifecycleSvc().createAndStart(statementSpec, queries.get(queryposition), false, statementName, userObject, null, statementId, null);
		queryposition++;
		log.debug(".createEPLStmt Statement created and started");
	        return statement;
	}
        

    }

    public EPStatement create(EPStatementObjectModel sodaStatement) throws EPException
    {
        return create(sodaStatement, null);
    }

    public EPStatement createModelStatementId(EPStatementObjectModel sodaStatement, String statementName, Object userObject, String statementId) throws EPException {
        return create(sodaStatement, statementName, userObject, statementId);
    }

    public EPStatement create(EPStatementObjectModel sodaStatement, String statementName, Object userObject) throws EPException {
        return create(sodaStatement, statementName, userObject, null);
    }

    public EPStatement create(EPStatementObjectModel sodaStatement, String statementName, Object userObject, String statementId) throws EPException
    {
        // Specifies the statement
        StatementSpecRaw statementSpec = mapSODAToRaw(sodaStatement);
        String eplStatement = sodaStatement.toEPL();

        EPStatement statement = services.getStatementLifecycleSvc().createAndStart(statementSpec, eplStatement, false, statementName, userObject, null, statementId, sodaStatement);

        log.debug(".createEPLStmt Statement created and started");
        return statement;
    }

    public EPStatement create(EPStatementObjectModel sodaStatement, String statementName) throws EPException
    {
        // Specifies the statement
        StatementSpecRaw statementSpec = mapSODAToRaw(sodaStatement);
        String eplStatement = sodaStatement.toEPL();

        EPStatement statement = services.getStatementLifecycleSvc().createAndStart(statementSpec, eplStatement, false, statementName, null, null, null, sodaStatement);

        log.debug(".createEPLStmt Statement created and started");
        return statement;
    }

    public EPPreparedStatement prepareEPL(String eplExpression) throws EPException
    {
        // compile to specification
        StatementSpecRaw statementSpec = EPAdministratorHelper.compileEPL(eplExpression, eplExpression, true, null, services, defaultStreamSelector);

        // map to object model thus finding all substitution parameters and their indexes
        StatementSpecUnMapResult unmapped = StatementSpecMapper.unmap(statementSpec);

        // the prepared statement is the object model plus a list of substitution parameters
        // map to specification will refuse any substitution parameters that are unfilled
        return new EPPreparedStatementImpl(unmapped.getObjectModel(), unmapped.getIndexedParams());
    }

    public EPPreparedStatement preparePattern(String patternExpression) throws EPException
    {
        StatementSpecRaw rawPattern = EPAdministratorHelper.compilePattern(patternExpression, patternExpression, true, services, SelectClauseStreamSelectorEnum.ISTREAM_ONLY);

        // map to object model thus finding all substitution parameters and their indexes
        StatementSpecUnMapResult unmapped = StatementSpecMapper.unmap(rawPattern);

        // the prepared statement is the object model plus a list of substitution parameters
        // map to specification will refuse any substitution parameters that are unfilled
        return new EPPreparedStatementImpl(unmapped.getObjectModel(), unmapped.getIndexedParams());
    }

    public EPStatement create(EPPreparedStatement prepared, String statementName, Object userObject, String statementId) throws EPException
    {
        EPPreparedStatementImpl impl = (EPPreparedStatementImpl) prepared;

        StatementSpecRaw statementSpec = mapSODAToRaw(impl.getModel());
        String eplStatement = impl.getModel().toEPL();

        return services.getStatementLifecycleSvc().createAndStart(statementSpec, eplStatement, false, statementName, userObject, null, statementId, impl.getModel());
    }

    public EPStatement create(EPPreparedStatement prepared, String statementName) throws EPException
    {
        return create(prepared, statementName, null, null);
    }

    public EPStatement create(EPPreparedStatement prepared, String statementName, Object userObject) throws EPException {
        return create(prepared, statementName, userObject, null);
    }

    public EPStatement createPreparedEPLStatementId(EPPreparedStatementImpl prepared, String statementName, Object userObject, String statementId) throws EPException {
        return create(prepared, statementName, userObject, statementId);
    }

    public EPStatement create(EPPreparedStatement prepared) throws EPException
    {
        return create(prepared, null);
    }

    public EPStatementObjectModel compileEPL(String eplStatement) throws EPException
    {
        StatementSpecRaw statementSpec = EPAdministratorHelper.compileEPL(eplStatement, eplStatement, true, null, services, defaultStreamSelector);
        StatementSpecUnMapResult unmapped = StatementSpecMapper.unmap(statementSpec);
        if (unmapped.getIndexedParams().size() != 0)
        {
            throw new EPException(SUBS_PARAM_INVALID_USE);
        }
        return unmapped.getObjectModel();
    }

    public EPStatement getStatement(String name)
    {
        return services.getStatementLifecycleSvc().getStatementByName(name);
    }

    public String getStatementNameForId(String statementId) {
        return services.getStatementLifecycleSvc().getStatementNameById(statementId);
    }

    public String[] getStatementNames()
    {
        return services.getStatementLifecycleSvc().getStatementNames();
    }

    public void startAllStatements() throws EPException
    {
        services.getStatementLifecycleSvc().startAllStatements();
    }

    public void stopAllStatements() throws EPException
    {
        services.getStatementLifecycleSvc().stopAllStatements();
    }

    public void destroyAllStatements() throws EPException
    {
        services.getStatementLifecycleSvc().destroyAllStatements();
    }

    public ConfigurationOperations getConfiguration()
    {
        return configurationOperations;
    }

    /**
     * Destroys an engine instance.
     */
    public void destroy()
    {
        services = null;
        configurationOperations = null;
    }

    public StatementSpecRaw compileEPLToRaw(String epl) {
        return EPAdministratorHelper.compileEPL(epl, epl, true, null, services, defaultStreamSelector);
    }

    public EPStatementObjectModel mapRawToSODA(StatementSpecRaw raw) {
        StatementSpecUnMapResult unmapped = StatementSpecMapper.unmap(raw);
        if (unmapped.getIndexedParams().size() != 0)
        {
            throw new EPException(SUBS_PARAM_INVALID_USE);
        }
        return unmapped.getObjectModel();
    }

    public StatementSpecRaw mapSODAToRaw(EPStatementObjectModel model) {
        return StatementSpecMapper.map(model, services.getEngineImportService(), services.getVariableService(), services.getConfigSnapshot(), services.getSchedulingService(), services.getEngineURI(), services.getPatternNodeFactory(), services.getNamedWindowService(), services.getContextManagementService(), services.getExprDeclaredService());
    }

    public EvalFactoryNode compilePatternToNode(String pattern) throws EPException
    {
        StatementSpecRaw raw = EPAdministratorHelper.compilePattern(pattern, pattern, false, services, SelectClauseStreamSelectorEnum.ISTREAM_ONLY);
        return ((PatternStreamSpecRaw) raw.getStreamSpecs().get(0)).getEvalFactoryNode();
    }

    public EPStatementObjectModel compilePatternToSODAModel(String expression) throws EPException {
        StatementSpecRaw rawPattern = EPAdministratorHelper.compilePattern(expression, expression, true, services, SelectClauseStreamSelectorEnum.ISTREAM_ONLY);
        return mapRawToSODA(rawPattern);
    }

    public ExprNode compileExpression(String expression) throws EPException
    {
        String toCompile = "select * from java.lang.Object.win:time(" + expression + ")";
        StatementSpecRaw raw = EPAdministratorHelper.compileEPL(toCompile, expression, false, null, services, SelectClauseStreamSelectorEnum.ISTREAM_ONLY);
        return raw.getStreamSpecs().get(0).getViewSpecs()[0].getObjectParameters().get(0);
    }

    public Expression compileExpressionToSODA(String expression) throws EPException
    {
        ExprNode node = compileExpression(expression);
        return StatementSpecMapper.unmap(node);
    }

    public PatternExpr compilePatternToSODA(String expression) throws EPException
    {
        EvalFactoryNode node = compilePatternToNode(expression);
        return StatementSpecMapper.unmap(node);
    }

    public AnnotationPart compileAnnotationToSODA(String annotationExpression)
    {
        String toCompile = annotationExpression + " select * from java.lang.Object";
        StatementSpecRaw raw = EPAdministratorHelper.compileEPL(toCompile, annotationExpression, false, null, services, SelectClauseStreamSelectorEnum.ISTREAM_ONLY);
        return StatementSpecMapper.unmap(raw.getAnnotations().get(0));
    }

    public MatchRecognizeRegEx compileMatchRecognizePatternToSODA(String matchRecogPatternExpression)
    {
        String toCompile = "select * from java.lang.Object match_recognize(measures a.b as c pattern (" + matchRecogPatternExpression + ") define A as true)";
        StatementSpecRaw raw = EPAdministratorHelper.compileEPL(toCompile, matchRecogPatternExpression, false, null, services, SelectClauseStreamSelectorEnum.ISTREAM_ONLY);
        return StatementSpecMapper.unmap(raw.getMatchRecognizeSpec().getPattern());
    }

    public EPContextPartitionAdmin getContextPartitionAdmin() {
        return new EPContextPartitionAdminImpl(services);
    }

    private static Log log = LogFactory.getLog(EPAdministratorImpl.class);
}
