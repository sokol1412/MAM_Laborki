package mam_wadim_sokolowski.mam_lab2_zad1;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private float accelerometer[] = new float[3];
    private float magnetic[] = new float[3];
    private float result[] = new float[3];
    private TextView resultView;
    private EditText editTextX;
    private EditText editTextY;
    private EditText editTextZ;

    private SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultView = (TextView) findViewById(R.id.resultView);
        editTextX = (EditText) findViewById(R.id.editTextX);
        editTextY = (EditText) findViewById(R.id.editTextY);
        editTextZ = (EditText) findViewById(R.id.editTextZ);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (accSensor != null)
            sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if (magSensor != null)
            sensorManager.registerListener(this, magSensor, SensorManager.SENSOR_DELAY_NORMAL);

        final Button convertButton = (Button) findViewById(R.id.convertButton);
        convertButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                convert();
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private void convert()
    {
        if (!isEmpty(editTextX) && !isEmpty(editTextY) && !isEmpty(editTextZ))
        {
            float[] R = new float[9];
            SensorManager.getRotationMatrix(R, null, accelerometer, magnetic);
            result[0] = Float.parseFloat(editTextX.getText().toString()) * R[0] +
                    Float.parseFloat(editTextY.getText().toString()) + R[1] +
                    Float.parseFloat(editTextZ.getText().toString()) + R[2];
            result[1] = Float.parseFloat(editTextX.getText().toString()) * R[3] +
                    Float.parseFloat(editTextY.getText().toString()) + R[4] +
                    Float.parseFloat(editTextZ.getText().toString()) + R[5];
            result[2] = Float.parseFloat(editTextX.getText().toString()) * R[6] +
                    Float.parseFloat(editTextY.getText().toString()) + R[7] +
                    Float.parseFloat(editTextZ.getText().toString()) + R[8];
            resultView.setText(result[0] + "\n" + result[1] + "\n" + result[2]);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometer[0] = event.values[0];
            accelerometer[1] = event.values[1];
            accelerometer[2] = event.values[2];
        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetic[0] = event.values[0];
            magnetic[1] = event.values[1];
            magnetic[2] = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
