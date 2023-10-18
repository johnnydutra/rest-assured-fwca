package refactor;

import core.Hooks;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import static utils.Helpers.getToken;
import static utils.Helpers.resetDatabase;
import static io.restassured.RestAssured.requestSpecification;

@RunWith(Suite.class)
@SuiteClasses({
    AccountTest.class,
    TransactionTest.class,
    BalanceTest.class,
    AuthenticationTest.class
})

public class RegressionSuite extends Hooks {

    @BeforeClass
    public static void login() {
        String ACCESS_TOKEN = getToken(SEU_BARRIGA_EMAIL, SEU_BARRIGA_PASSWORD);
        requestSpecification.header("Authorization", "JWT " + ACCESS_TOKEN);

        resetDatabase();
    }

}
