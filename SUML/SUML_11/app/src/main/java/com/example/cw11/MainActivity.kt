package com.example.cw11

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.nio.FloatBuffer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val inputEditText = findViewById<EditText>(R.id.edtPointX)
        val outputEditText = findViewById<EditText>(R.id.edtpointY)
        val buttonPredict = findViewById<Button>(R.id.btnPredict)

        buttonPredict.setOnClickListener {
            val inputs = inputEditText.text.toString().toFloatOrNull()
            if(inputs != null){
                val ortEnvironment = OrtEnvironment.getEnvironment()
                val ortSession = createORTSession(ortEnvironment)
                val output = runPredict(inputs, ortSession, ortEnvironment)
                outputEditText.setText(output.toString())
            }else{
                Toast.makeText(this, "Please check the inputs", Toast.LENGTH_LONG).show()
            }
        }
    }
    data class Point(
        val x: Int,
        val y: Int,
    )
    private fun createORTSession(ortEnvironment: OrtEnvironment): OrtSession {
        val modelBytes = resources.openRawResource(R.raw.our_model).readBytes()
        return ortEnvironment.createSession(modelBytes)
    }
    private fun runPredict(input: Float, ortSession: OrtSession, ortEnvironment: OrtEnvironment): Float{
        val inputName = ortSession.inputNames?.iterator()?.next()
        val floatBufferInputs = FloatBuffer.wrap((floatArrayOf(input)))
        val inputTensor = OnnxTensor.createTensor(ortEnvironment,floatBufferInputs,longArrayOf(1,1))
        val results = ortSession.run(mapOf(inputName to inputTensor))
        val output = results[0].value as Array<FloatArray>
        return output[0][0]
    }
}