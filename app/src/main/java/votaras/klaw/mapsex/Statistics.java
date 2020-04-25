package votaras.klaw.mapsex;

import android.content.Intent;
import android.os.Bundle;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Statistics extends AppCompatActivity {

    private EditText Statistics;
    private Button btn_statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
    };

    public void onButtonClick(View v){
        EditText Statistics = (EditText)findViewById(R.id.txtStatistics);
    };
}