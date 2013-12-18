package com.opentaxi.android;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.TextRule;
import com.opentaxi.android.simplefacebook.Permissions;
import com.opentaxi.android.simplefacebook.SimpleFacebook;
import com.opentaxi.android.simplefacebook.SimpleFacebookConfiguration;
import com.opentaxi.android.simplefacebook.entities.Profile;
import com.opentaxi.android.simplefacebook.entities.Work;
import com.opentaxi.android.utils.AppPreferences;
import com.opentaxi.generated.mysql.tables.pojos.Contact;
import com.opentaxi.generated.mysql.tables.pojos.Contactaddress;
import com.opentaxi.generated.mysql.tables.pojos.FacebookUsers;
import com.opentaxi.models.NewUsers;
import com.opentaxi.models.Users;
import com.opentaxi.rest.RestClient;
import com.taxibulgaria.enums.Gender;
import org.androidannotations.annotations.*;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: stanimir
 * Date: 1/8/13
 * Time: 8:02 PM
 * To change this template use File | Settings | File Templates.
 */
@EActivity(R.layout.login)
public class UserPassActivity extends Activity implements Validator.ValidationListener {

    private static final String TAG = "UserPassActivity";

    private static final int RESULT_NEW_CLIENT = 1;
    private static final int RESULT_LOST_PASSWORD = 2;

    /*@ViewById(R.id.clientLoginButton)
    Button submitButton;*/

    @TextRule(order = 1, minLength = 3, message = "Username is too short.Enter at least 3 characters.")
    @ViewById(R.id.userNameField)
    EditText userName;

    @TextRule(order = 2, minLength = 6, message = "Password is too short.Enter at least 6 characters.")
    @ViewById(R.id.passwordField)
    EditText pass;

    Validator validator;

    SimpleFacebook mSimpleFacebook;
    private int result = Activity.RESULT_OK;

    private static final int SERVER_CHANGE = 12;

    @Override
    public void onBackPressed() {
        result = Activity.RESULT_CANCELED;
        super.onBackPressed();
    }

    @Override
    public void finish() {
        if (result != Activity.RESULT_CANCELED) result = Activity.RESULT_OK;
        if (getParent() == null) {
            setResult(result);
        } else {
            getParent().setResult(result);
        }
        super.finish();
    }
    /*@InstanceState
    Bundle savedInstanceState;

    private Session.StatusCallback statusCallback = new SessionStatusCallback();



    @Override
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(statusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(statusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }*/

    @AfterViews
    void afterLoad() {
        //submitButton.setClickable(true);
        result = Activity.RESULT_OK;
        validator = new Validator(this);
        validator.setValidationListener(this);

        pass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(final View v, final int keyCode, final KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    validator.validateAsync();
                    //return true; //this will keep keyboard open
                }

                return false;
            }
        });
        /*Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
            }
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.options_server:
                Intent intent = new Intent(this, ServersActivity_.class);
                startActivityForResult(intent, SERVER_CHANGE);
                return true;

            case R.id.options_exit:

                finish();
                return true;
            case R.id.options_send_log:

                String javaTmpDir = System.getProperty("java.io.tmpdir");
                File cacheDir = new File(javaTmpDir, "DiskLruCacheDir");
                if (cacheDir.exists()) {
                    File[] files = cacheDir.listFiles();
                    if (files != null)
                        for (File file : files) {
                            file.delete();
                        }
                }
                int i = 2 / 0;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSimpleFacebook != null)
            mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            Log.i("LoginUsingLoginFragmentActivity:" + session.getAccessToken(), String.format("New session state: %s", state.toString()));
        }
    }*/

    /*@OnActivityResult(RESULT_NEW_CLIENT)
    void onResult(int resultCode, Intent data) {

    }*/

    /*@Click
    void clientLoginButton() {
        submitButton.setClickable(false);
        validator.validateAsync();
    }*/

    @Click
    void facebookButton() {

        mSimpleFacebook = SimpleFacebook.getInstance(this); // Permissions.USER_BIRTHDAY
        Permissions[] permissions = new Permissions[]{Permissions.EMAIL, Permissions.USER_WEBSITE, Permissions.USER_WORK_HISTORY, Permissions.USER_ABOUT_ME, Permissions.USER_HOMETOWN};
        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                //.setAppId("550947981660612")
                .setNamespace("taxi-bulgaria")
                .setPermissions(permissions)
                .build();
        SimpleFacebook.setConfiguration(configuration);

        SimpleFacebook.OnLoginListener onLoginListener = new SimpleFacebook.OnLoginListener() {

            @Override
            public void onLogin() {

                //Log.e(TAG, "onLogin");
                if (AppPreferences.getInstance() != null)
                    AppPreferences.getInstance().setAccessToken(mSimpleFacebook.getAccessToken());  //todo move this to disk cache
                checkFacebook(mSimpleFacebook.getAccessToken());
            }

            @Override
            public void onNotAcceptingPermissions() {
                Log.e(TAG, "onNotAcceptingPermissions token:" + mSimpleFacebook.getAccessToken());
            }

            @Override
            public void onThinking() {
                Log.i(TAG, "onThinking");
            }

            @Override
            public void onException(Throwable throwable) {
                Log.e(TAG, "onException:" + throwable.getMessage());
                //facebookLogout();
            }

            @Override
            public void onFail(String reason) {
                Log.e(TAG, "onFail:" + reason);
            }
        };

        mSimpleFacebook.login(onLoginListener);
        Log.i(TAG, "mSimpleFacebook.login:" + mSimpleFacebook.getAccessToken());

        /*Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true, statusCallback);
        }*/
    }

    @Background
    void checkFacebook(String token) {
        Log.i(TAG, "checkFacebook token:" + token);
        facebookUser(RestClient.getInstance().FacebookLogin(token));
    }

    @UiThread
    void facebookUser(Users user) {
        if (user != null) { //user already exist in taxi-bulgaria service
            Log.i(TAG, "facebookUser user:" + user.getUsername());
            if (user.getId() != null && user.getId() > 0) {
                RestClient.getInstance().setAuthHeadersEncoded(user.getUsername(), user.getPassword());
                AppPreferences.getInstance().setUsers(user);
                finish();
            } else setError("Грешно потребителско име или парола!");

            //facebookLogout();
        } else { //new Facebook User
            Log.i(TAG, "New facebookUser");
            SimpleFacebook.OnProfileRequestListener onProfileRequestListener = new SimpleFacebook.OnProfileRequestListener() {

                @Override
                public void onFail(String reason) {
                    Log.i(TAG, "New facebookUser onFail");
                }

                @Override
                public void onException(Throwable throwable) {
                    Log.i(TAG, "New facebookUser onException");
                    //facebookLogout();
                }

                @Override
                public void onThinking() {
                }

                @Override
                public void onComplete(Profile profile) {

                    if (profile != null) { //&& profile.getVerified()) {
                        NewUsers users = new NewUsers();
                        users.setUsername(profile.getUsername());
                        users.setEmail(profile.getEmail());
                        users.setImage(profile.getPicture());

                        Contact contact = new Contact();
                        contact.setFirstname(profile.getFirstName());
                        contact.setMiddlename(profile.getMiddleName());
                        contact.setLastname(profile.getLastName());
                        contact.setNotes(profile.getBio());
                        if (profile.getWork() != null) {
                            for (Work work : profile.getWork()) {
                                String workTitle = work.getEmployer() + " " + work.getDescription();
                                if (contact.getJobtitle() != null)
                                    contact.setJobtitle(contact.getJobtitle() + " " + workTitle);
                                else contact.setJobtitle(workTitle);
                            }
                        }
                        if (profile.getGender() != null) {
                            if (profile.getGender().equals("male"))
                                contact.setGender(Gender.MALE.getCode());
                            else if (profile.getGender().equals("female")) contact.setGender(Gender.FEMALE.getCode());
                        }

                        users.setContact(contact);

                        Contactaddress address = new Contactaddress();
                        address.setCity(profile.getHometown());
                        users.setAddress(address);

                        FacebookUsers facebookUsers = new FacebookUsers();
                        facebookUsers.setFacebookId(Long.parseLong(profile.getId()));
                        facebookUsers.setToken(mSimpleFacebook.getAccessToken());
                        users.setfUsers(facebookUsers);

                        Intent newClient = new Intent(UserPassActivity.this, NewClientActivity_.class);
                        newClient.putExtra("newUsers", users);
                        newClient.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        UserPassActivity.this.startActivity(newClient);
                        finish();
                    } else Log.e(TAG, "profile is null or not verified");

                    //facebookLogout();
                }

            };

            mSimpleFacebook.getProfile(onProfileRequestListener);
        }
    }

    @Background
    void facebookLogout() {
        SimpleFacebook.OnLogoutListener onLogoutListener = new SimpleFacebook.OnLogoutListener() {

            @Override
            public void onFail(String reason) {
            }

            @Override
            public void onException(Throwable throwable) {
            }

            @Override
            public void onThinking() {
            }

            @Override
            public void onLogout() {
            }

        };
        mSimpleFacebook.logout(onLogoutListener);
    }

    @Click
    void newClient() {
        Log.i("newClient", "newClient");

        NewClientActivity_.intent(this).startForResult(RESULT_NEW_CLIENT);
    }

    @Click
    void lostPassword() {
        Log.i("lostPassword", "lostPassword");

        LostPasswordActivity_.intent(this).startForResult(RESULT_LOST_PASSWORD);
    }

    @Background
    void login(String username, String password) {

        Log.i("Login", "user:" + username + " pass:" + password);
        Users user = RestClient.getInstance().Login(username, password);

        if (user != null) {
            if (user.getId() != null && user.getId() > 0) {
                //users = user;
                //AppPreferences.getInstance().setUsers(user);
                if (AppPreferences.getInstance() != null) {
                    try {
                        String userEncrypt = AppPreferences.getInstance().encrypt(username, "user_salt");
                        String passEncrypt = AppPreferences.getInstance().encrypt(password, username);
                        RestClient.getInstance().saveAuthorization(userEncrypt, passEncrypt);
                    } catch (Exception e) {
                        if (e.getMessage() != null) Log.e(TAG, "Exception:" + e.getMessage());
                    }
                }
                //Toast.makeText(UserPassActivity.this, "Влязохте в системата успешно!", Toast.LENGTH_LONG).show();
                finish();
            } else setError("Грешно потребителско име или парола!");
            // Toast.makeText(UserPassActivity.this, "Грешно потребителско име или парола!", Toast.LENGTH_LONG).show();

        } else setError("Грешка! Сигурни ли сте че имате връзка с интернет?");
        //Toast.makeText(UserPassActivity.this, "Грешка! Сигурни ли сте че имате връзка с интернет?", Toast.LENGTH_LONG).show();
    }

    @UiThread
    void setError(String error) {
        pass.setError(error);
        //submitButton.setClickable(true);
    }

    @Override
    public void onValidationSucceeded() {
        login(userName.getText().toString(), pass.getText().toString());
    }

    @Override
    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();

        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        //submitButton.setClickable(true);
    }
}
