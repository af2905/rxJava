package ru.job4j.rxjava;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {
    private Disposable sbr;
    private ProgressBar bar;
    private TextView info;
    private Button start;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        bar = findViewById(R.id.load);
        info = findViewById(R.id.info);
        boolean recreated = bundle != null;
        final int startAt = recreated ? bundle.getInt("progress", 0) : 0;
        info.setText(String.format(Locale.getDefault(), "%d%%", startAt));
        start = findViewById(R.id.start);
        start.setOnClickListener(v -> start(startAt));
        Button stop = findViewById(R.id.stop);
        stop.setOnClickListener(v -> stop());
        if (recreated) {
            start(startAt);
        }
    }

    public void start(int startAt) {
        this.sbr = Observable
                .interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> {
                    info.setText(String.format(
                            Locale.getDefault(), "%d%%", startAt + v.intValue()));
                    bar.setProgress(startAt + v.intValue());
                });
        start.setEnabled(false);
    }

    public void stop() {
        sbr.dispose();
        start.setEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("progress", bar.getProgress());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.sbr != null) {
            this.sbr.dispose();
        }
    }
}



