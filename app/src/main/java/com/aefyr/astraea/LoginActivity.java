package com.aefyr.astraea;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.aefyr.astraea.custom.AnimatedTextView;
import com.aefyr.astraea.custom.ProgressIndicator;
import com.aefyr.astraea.helpers.AccountManager;
import com.aefyr.astraea.utility.ViewUtils;
import com.aefyr.sombra.account.AccountData;
import com.aefyr.sombra.account.AccountHelper;
import com.aefyr.sombra.common.ApiError;
import com.aefyr.sombra.common.SombraCore;
import com.aefyr.sombra.diary.BoundStudent;
import com.aefyr.sombra.diary.Diary;
import com.aefyr.sombra.diary.Hometask;
import com.aefyr.sombra.diary.HomeworkDay;
import com.aefyr.sombra.diary.Period;
import com.aefyr.sombra.diary.ScheduleDay;
import com.aefyr.sombra.diary.ScheduleLesson;
import com.aefyr.sombra.diary.StudentsHelper;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button signIn;
    private TextView stat;
    private AnimatedTextView message;
    private ProgressIndicator progressIndicator;

    private String sId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signIn = (Button) findViewById(R.id.signIn);
        stat = (TextView) findViewById(R.id.stat);
        message = (AnimatedTextView) findViewById(R.id.message);
        progressIndicator = (ProgressIndicator) findViewById(R.id.indicator);

        progressIndicator.setColorScheme(Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA);

        ((ToggleButton) findViewById(R.id.toggleButton)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showProgress(isChecked);
            }
        });

        username.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                return Pattern.matches("[^0-9]", source) ? "" : null;
            }
        }});

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                clearError();
            }
        };

        username.addTextChangedListener(watcher);
        password.addTextChangedListener(watcher);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new AuthSimulator().execute();
                showProgress(true);
                setMessage("Authorizing");
                final AccountManager accountManager = AccountManager.getInstance(LoginActivity.this);
                new AccountHelper(SombraCore.getInstance(LoginActivity.this)).authorize(username.getText().toString(), password.getText().toString(), new AccountHelper.AuthorizationListener() {
                    @Override
                    public void onSuccess(String result) {
                        stat.setText(result);
                        accountManager.setToken(result);
                        setMessage("Fetching Data (1/5)");

                        new AccountHelper(SombraCore.getInstance(LoginActivity.this, AccountManager.getInstance(LoginActivity.this).getToken())).getProfileInfo(new AccountHelper.ProfileListener() {
                            @Override
                            public void onSuccess(AccountData result) {
                                accountManager.setAccountData(result);
                                stat.setText("name: " + accountManager.getNormalName() + "\nmail: " + accountManager.getEmail());
                                setMessage("Fetching Data (2/5)");

                                new StudentsHelper(SombraCore.getInstance(LoginActivity.this, AccountManager.getInstance(LoginActivity.this).getToken())).getBoundStudents(new StudentsHelper.StudentsGetListener() {
                                    @Override
                                    public void onSuccess(ArrayList<BoundStudent> result) {
                                        StringBuilder names = new StringBuilder();
                                        for (BoundStudent student : result)
                                            names.append(student.name()).append("\n");
                                        stat.setText(names);
                                        setMessage("Fetching Data (3/5)");
                                        sId = result.get(0).id();

                                        new Diary(SombraCore.getInstance(LoginActivity.this, AccountManager.getInstance(LoginActivity.this).getToken())).getSchedule(sId, "2017-09-01", "2017-09-07", new Diary.ScheduleListener() {
                                            @Override
                                            public void onSuccess(ArrayList<ScheduleDay> result) {
                                                StringBuilder days = new StringBuilder();
                                                for(ScheduleDay day: result){
                                                    days.append(day.rawDate()).append("\n");
                                                    for(ScheduleLesson lesson: day.lessons()){
                                                        days.append(lesson.num()).append(". ").append(lesson.name()).append("\n");
                                                    }
                                                }
                                                stat.setText(days.toString());
                                                setMessage("Fetching Data (4/5)");

                                                new Diary(SombraCore.getInstance(LoginActivity.this, AccountManager.getInstance(LoginActivity.this).getToken())).getMarks(sId, 0, new Diary.MarksListener() {
                                                    @Override
                                                    public void onSuccess(ArrayList<Period> result) {
                                                        StringBuilder periods = new StringBuilder();
                                                        for(Period p: result){
                                                            periods.append(p.subjectName()).append("\n");
                                                        }
                                                        stat.setText(periods.toString());
                                                        setMessage("Fetching Data (5/5)");

                                                        new Diary(SombraCore.getInstance(LoginActivity.this, AccountManager.getInstance(LoginActivity.this).getToken())).getHomework(sId, "2017-09-01", "2017-09-07", new Diary.HomeworkListener() {
                                                            @Override
                                                            public void onSuccess(ArrayList<HomeworkDay> result) {
                                                                StringBuilder homework = new StringBuilder();
                                                                for(HomeworkDay day: result){
                                                                    homework.append(day.rawDate()).append("\n");
                                                                    for(Hometask task: day.tasks()){
                                                                        homework.append(task.subjectName()).append("\n").append(task.task()).append("\n");
                                                                    }
                                                                }
                                                                stat.setText(homework.toString());
                                                                showProgress(false);
                                                                setMessage("Done!");
                                                            }

                                                            @Override
                                                            public void onNetworkError() {
                                                                showError(getString(R.string.error_network));
                                                                showProgress(false);
                                                            }

                                                            @Override
                                                            public void onInvalidTokenError() {
                                                                showError(getString(R.string.error_token));
                                                                showProgress(false);
                                                            }

                                                            @Override
                                                            public void onApiError(ApiError error) {
                                                                showError(String.format(getString(R.string.error_api), error.getMessage()));
                                                                showProgress(false);
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onNetworkError() {
                                                        showError(getString(R.string.error_network));
                                                        showProgress(false);
                                                    }

                                                    @Override
                                                    public void onInvalidTokenError() {
                                                        showError(getString(R.string.error_token));
                                                        showProgress(false);
                                                    }

                                                    @Override
                                                    public void onApiError(ApiError error) {
                                                        showError(String.format(getString(R.string.error_api), error.getMessage()));
                                                        showProgress(false);
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onNetworkError() {
                                                showError(getString(R.string.error_network));
                                                showProgress(false);
                                            }

                                            @Override
                                            public void onInvalidTokenError() {
                                                showError(getString(R.string.error_token));
                                                showProgress(false);
                                            }

                                            @Override
                                            public void onApiError(ApiError error) {
                                                showError(String.format(getString(R.string.error_api), error.getMessage()));
                                                showProgress(false);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onNetworkError() {
                                        showError(getString(R.string.error_network));
                                        showProgress(false);
                                    }

                                    @Override
                                    public void onInvalidTokenError() {
                                        showError(getString(R.string.error_token));
                                        showProgress(false);
                                    }

                                    @Override
                                    public void onApiError(ApiError error) {
                                        showError(String.format(getString(R.string.error_api), error.getMessage()));
                                        showProgress(false);
                                    }
                                });
                            }

                            @Override
                            public void onNetworkError() {
                                showError(getString(R.string.error_network));
                                showProgress(false);
                            }

                            @Override
                            public void onInvalidTokenError() {
                                showError(getString(R.string.error_token));
                                showProgress(false);
                            }

                            @Override
                            public void onApiError(ApiError error) {
                                showError(String.format(getString(R.string.error_api), error.getMessage()));
                                showProgress(false);
                            }
                        });
                    }

                    @Override
                    public void onInvalidCredentials() {
                        showError(getString(R.string.error_invalid_credentials));
                        showProgress(false);
                    }

                    @Override
                    public void onNetworkError() {
                        showError(getString(R.string.error_network));
                        showProgress(false);
                    }

                    @Override
                    public void onInvalidTokenError() {
                        //Never gonna happen
                    }

                    @Override
                    public void onApiError(ApiError error) {
                        showError(String.format(getString(R.string.error_api), error.getMessage()));
                        showProgress(false);
                    }
                });
            }
        });
    }

    class AuthSimulator extends AsyncTask<Void, Integer, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setMessage("Authorizing");
            showProgress(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i <= 10; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            setMessage(String.format(Locale.getDefault(), "Fetching data (%d/10)", values[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showProgress(false);
            setMessage("Authorized!");
        }
    }

    private void auth() {

    }

    private void authCompleted(boolean successful) {

    }

    private void showProgress(boolean show) {
        username.setEnabled(!show);
        password.setEnabled(!show);
        signIn.setEnabled(!show);
        if (show)
            progressIndicator.show();
        else
            progressIndicator.hide();
    }

    private void showError(String error) {
        setMessage(error);
        ViewUtils.highLightTV(getResources(), message);
    }

    private void clearError() {
        setMessage(getString(R.string.number_hint));
    }

    private void setMessage(String messageText) {
        message.setText(messageText, true);
    }
}
