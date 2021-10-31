package com.example.WorkoutApp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_b_m_i.*
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    val metricUnitsView = "METRIC_UNIT_VIEW"
    val usUnitsView = "US_UNIT_VIEW"
    val collUnitsView = "COLL_UNIT_VIEW"
    var currentVisibleView: String = metricUnitsView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_m_i)

        setSupportActionBar(toolbar_bmi_activity)
        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "CALCULATE BMI"
        }
        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }
        btn_calculate_units.setOnClickListener {
            if (currentVisibleView.equals(metricUnitsView)) {
                if (validateMetricUnits()) {
                    val heightValue: Float = et_metricUnitHeight.text.toString().toFloat() / 100
                    val weightValue: Float = et_metricUnitWeight.text.toString().toFloat()
                    val bmi = weightValue / (heightValue * heightValue)
                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(
                        this@BMIActivity,
                        "Please enter valid values",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else if (currentVisibleView.equals(usUnitsView)) {
                if (validateUSUnits()) {
                    val usUnitHeightValueFeet: String = et_us_unit_height_feet.text.toString()
                    val usUnitHeightValueInch: String = et_us_unit_height_inch.text.toString()
                    val usUnitWeightValue: Float = et_us_unit_weight.text.toString().toFloat()
                    val heightValue =
                        usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12
                    val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))
                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(
                        this@BMIActivity,
                        "Please enter valid values",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                if (validateCollUnits()){
                    val collUnitHeightValueFeet : String = et_coll_unit_height_feet.text.toString()
                    val collUnitHeightValueInch: String = et_coll_unit_height_inch.text.toString()
                    val collUnitWeightValue: Float = et_coll_unit_weight.text.toString().toFloat()
                    val heightValue: Float = (((collUnitHeightValueFeet.toFloat()*30.48)+(collUnitHeightValueInch.toFloat()*2.54))/100).toFloat()
                    val bmi = (collUnitWeightValue/(heightValue*heightValue))
                    displayBMIResult(bmi)
                }else{
                    Toast.makeText(
                        this@BMIActivity,
                        "Please enter valid values",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        makeVisibleMetricUnitsView()
        rg_units.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.rb_metric_units){
                makeVisibleMetricUnitsView()
            }else if (checkedId == R.id.rb_us_units){
                makeVisibleUSUnitsView()
            }else{
                makeVisibleCollUnitsView()
            }
        }
    }

    private fun makeVisibleUSUnitsView(){
        currentVisibleView = usUnitsView
        til_metricUnitWeight.visibility = View.GONE
        til_metricUnitHeight.visibility = View.GONE
        til_coll_unit_weight.visibility = View.GONE
        ll_coll_units_height.visibility = View.GONE

        et_us_unit_weight.text!!.clear()
        et_us_unit_height_feet.text!!.clear()
        et_us_unit_height_inch.text!!.clear()

        til_us_unit_weight.visibility = View.VISIBLE
        ll_us_units_height.visibility = View.VISIBLE

        ll_display_bmi_result.visibility = View.INVISIBLE
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = metricUnitsView
        til_metricUnitWeight.visibility = View.VISIBLE
        til_metricUnitHeight.visibility = View.VISIBLE

        et_metricUnitHeight.text!!.clear()
        et_metricUnitWeight.text!!.clear()

        til_us_unit_weight.visibility = View.GONE
        ll_us_units_height.visibility = View.GONE
        til_coll_unit_weight.visibility = View.GONE
        ll_coll_units_height.visibility = View.GONE

        ll_display_bmi_result.visibility = View.INVISIBLE
    }

    private fun makeVisibleCollUnitsView(){
        currentVisibleView = collUnitsView
        til_coll_unit_weight.visibility = View.VISIBLE
        ll_coll_units_height.visibility = View.VISIBLE

        et_coll_unit_weight.text!!.clear()
        et_coll_unit_height_inch.text!!.clear()
        et_coll_unit_height_feet.text!!.clear()

        til_metricUnitWeight.visibility = View.GONE
        til_metricUnitHeight.visibility = View.GONE
        til_us_unit_weight.visibility = View.GONE
        ll_us_units_height.visibility = View.GONE
        ll_display_bmi_result.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi : Float){
        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0){
            bmiLabel = "VERY SEVERELY UnderWeight"
            bmiDescription = "You really need to take care of your health! Eat more and more!!"
        }else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "SEVERELY Underweight"
            bmiDescription = "You really need to take care of yourself! Eat more!!"
        }else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLabel = "Underweight"
            bmiDescription = "You really need to take care of yourself! Eat some more!!"
        }else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLabel = "NORMAL"
            bmiDescription = "Congrats! You are in good shape!!"
        }else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0){
            bmiLabel = "Overweight"
            bmiDescription = "You need to do some workout!!"
        }else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0){
            bmiLabel = "MODERATELY Obese"
            bmiDescription = "It's high time that you start the workout!!"
        }else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0){
            bmiLabel = "SEVERELY Obese"
            bmiDescription = "You are in a very dangerous position! Act now!!"
        }else{
            bmiLabel = "VERY SEVERELY Obese"
            bmiDescription = "You need help lol"
        }
        ll_display_bmi_result.visibility = View.VISIBLE
        //tv_your_bmi.visibility = View.VISIBLE
        //tv_bmi_value.visibility = View.VISIBLE
        //tv_bmi_type.visibility = View.VISIBLE
        //tv_bmi_description.visibility = View.VISIBLE

        val bmiValue = bmi.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toString()
        tv_bmi_value.text = bmiValue
        tv_bmi_type.text = bmiLabel
        tv_bmi_description.text = bmiDescription
    }

    private fun validateMetricUnits(): Boolean{
        var isValid = true
        if (et_metricUnitWeight.text.toString().isEmpty()){
            isValid = false
        }else if (et_metricUnitHeight.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun validateUSUnits(): Boolean{
        var isValid = true
        if (et_us_unit_weight.text.toString().isEmpty()){
            isValid = false
        }else if (et_us_unit_height_feet.text.toString().isEmpty()){
            isValid = false
        }else if (et_us_unit_height_inch.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun validateCollUnits(): Boolean{
        var isValid = true
        if (et_coll_unit_weight.text.toString().isEmpty()){
            isValid = false
        }else if (et_coll_unit_height_feet.text.toString().isEmpty()){
            isValid = false
        }else if (et_coll_unit_height_inch.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }
}