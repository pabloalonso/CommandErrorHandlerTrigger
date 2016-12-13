import junit.framework.Assert;
import org.apache.commons.io.IOUtils;
import org.bonitasoft.engine.api.*;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.identity.UserCriterion;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.util.APITypeManager;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.*;

/**
 * Created by pablo on 07/10/2016.
 */
public class TestCommandToGenerateIncident {
    private static final String HOST_NAME = "http://localhost:8080";
    private static final Serializable FLOWNODEID = 140089L;
    private static final String USER_NAME = "walter.bates";
    private static final String PASSWORD = "bpm";
    private final String COMMAND_PATH = "C:\\Proyectos\\DAON\\october 2\\CommandErrorHandlerTrigger\\target\\CommandErrorHandlerTrigger-1.0.jar";
    private LoginAPI loginAPI;
    private APISession apiSession;
    private CommandAPI commandAPI;
    private static String COMMAND_NAME = "CommandErrorHandlerTrigger";


    @AfterTest
    public void tierDown(){
        try {
            //undeploy
            commandAPI.unregister(COMMAND_NAME);
            commandAPI.removeDependency(COMMAND_NAME+"JAR");
            loginAPI.logout(apiSession);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @BeforeTest
    public void tierUp() throws Exception {
        Map<String, String> settings = new HashMap<String, String>();
        settings.put("server.url", HOST_NAME);
        settings.put("application.name", "bonita");
        APITypeManager.setAPITypeAndParams(ApiAccessType.HTTP, settings);
        try {
            loginAPI = TenantAPIAccessor.getLoginAPI();
            apiSession = loginAPI.login(USER_NAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

       byte[] byteArray = IOUtils.toByteArray(new FileInputStream(new File(COMMAND_PATH)));

        //deploy
        commandAPI = TenantAPIAccessor.getCommandAPI(apiSession);

        commandAPI.addDependency(COMMAND_NAME+"JAR", byteArray);
        commandAPI.register(COMMAND_NAME, "Genereate an incident log", "org.bonitasoft.ErrorCommand");
        System.out.println("Command published");

    }

    @Test
    public void testFileHandlerChange(){
        try {
            Map<String, String> settings = new HashMap<String, String>();
            settings.put("server.url", HOST_NAME);
            settings.put("application.name", "bonita");
            APITypeManager.setAPITypeAndParams(ApiAccessType.HTTP, settings);
            try {
                loginAPI = TenantAPIAccessor.getLoginAPI();
                apiSession = loginAPI.login(USER_NAME, PASSWORD);
            } catch (Exception e) {
                e.printStackTrace();
            }
            final Map<String, Serializable> parameters = new HashMap<String, Serializable>();

            parameters.put("flowNodeId", FLOWNODEID);
            Assert.assertNotNull(commandAPI.execute(COMMAND_NAME, parameters));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
