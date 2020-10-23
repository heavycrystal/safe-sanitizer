package com.example.firebasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

public class RegisterPhase1Activity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 420;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phase1);
        Log.d("P1", "Phase 1 started");

       startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.PhoneBuilder().build()))
                        .setIsSmartLockEnabled(false)
                        .setTosAndPrivacyPolicyUrls("youtube.com", "github.com")
                        .build(),
                RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, RegisterPhase2Activity.class);
                startActivity(intent);
                finish();
            }
            else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Snackbar.make(findViewById(android.R.id.content), R.string.sign_in_cancelled, Snackbar.LENGTH_LONG);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.no_internet_connection, Snackbar.LENGTH_LONG);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return;
                }

                Snackbar.make(findViewById(android.R.id.content), R.string.unknown_error, Snackbar.LENGTH_LONG);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return;
            }
        }
    }
}