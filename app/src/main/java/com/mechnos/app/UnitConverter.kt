package com.mechnos.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private data class EngineeringUnit(val symbol: String, val factor: Double = 1.0)
private data class UnitCategory(val fa: String, val en: String, val units: List<EngineeringUnit>, val temperature: Boolean = false)

private val unitCategories = listOf(
    UnitCategory("طول", "Length", listOf(EngineeringUnit("m",1.0),EngineeringUnit("mm",0.001),EngineeringUnit("cm",0.01),EngineeringUnit("km",1000.0),EngineeringUnit("in",0.0254),EngineeringUnit("ft",0.3048))),
    UnitCategory("جرم", "Mass", listOf(EngineeringUnit("kg",1.0),EngineeringUnit("g",0.001),EngineeringUnit("tonne",1000.0),EngineeringUnit("lb",0.45359237))),
    UnitCategory("نیرو", "Force", listOf(EngineeringUnit("N",1.0),EngineeringUnit("kN",1000.0),EngineeringUnit("MN",1e6),EngineeringUnit("lbf",4.4482216153))),
    UnitCategory("فشار", "Pressure", listOf(EngineeringUnit("Pa",1.0),EngineeringUnit("kPa",1000.0),EngineeringUnit("MPa",1e6),EngineeringUnit("bar",100000.0),EngineeringUnit("atm",101325.0),EngineeringUnit("psi",6894.757293))),
    UnitCategory("انرژی", "Energy", listOf(EngineeringUnit("J",1.0),EngineeringUnit("kJ",1000.0),EngineeringUnit("MJ",1e6),EngineeringUnit("Wh",3600.0),EngineeringUnit("kWh",3.6e6))),
    UnitCategory("توان", "Power", listOf(EngineeringUnit("W",1.0),EngineeringUnit("kW",1000.0),EngineeringUnit("MW",1e6),EngineeringUnit("hp",745.699872))),
    UnitCategory("سرعت", "Speed", listOf(EngineeringUnit("m/s",1.0),EngineeringUnit("km/h",1.0/3.6),EngineeringUnit("mph",0.44704),EngineeringUnit("ft/s",0.3048))),
    UnitCategory("دما", "Temperature", listOf(EngineeringUnit("°C"),EngineeringUnit("°F"),EngineeringUnit("K")), true),
    UnitCategory("زاویه", "Angle", listOf(EngineeringUnit("rad",1.0),EngineeringUnit("deg",Math.PI/180.0),EngineeringUnit("rev",2.0*Math.PI)))
)

private fun toBase(value: Double, category: UnitCategory, index: Int): Double {
    if (!category.temperature) return value * category.units[index].factor
    return when(index) { 0 -> value + 273.15; 1 -> (value - 32.0) * 5.0/9.0 + 273.15; else -> value }
}
private fun fromBase(value: Double, category: UnitCategory, index: Int): Double {
    if (!category.temperature) return value / category.units[index].factor
    return when(index) { 0 -> value - 273.15; 1 -> (value - 273.15) * 9.0/5.0 + 32.0; else -> value }
}
private fun formatConversion(value: Double): String {
    if (!value.isFinite()) return "—"
    return String.format(Locale.US, "%.8f", value).trimEnd('0').trimEnd('.').ifBlank { "0" }
}

@Composable
fun UnitConverterPage(fa: Boolean, fg: Color, muted: Color, surface: Color) {
    var categoryIndex by remember { mutableIntStateOf(0) }
    var fromIndex by remember { mutableIntStateOf(0) }
    var toIndex by remember { mutableIntStateOf(1) }
    var input by remember { mutableStateOf("1") }
    var fromExpanded by remember { mutableStateOf(false) }
    var toExpanded by remember { mutableStateOf(false) }
    val category = unitCategories[categoryIndex]
    val value = input.replace(',', '.').toDoubleOrNull()
    val converted = value?.let { fromBase(toBase(it, category, fromIndex), category, toIndex) }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(if(fa) "تبدیل واحد مهندسی" else "Engineering Unit Converter", color=fg, fontSize=23.sp, fontWeight=FontWeight.Bold)
        Text(if(fa) "تبدیل سریع کمیت‌های پرکاربرد در محاسبات مکانیک" else "Quick conversions for common mechanical engineering quantities", color=muted, fontSize=12.sp)
        Column(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
            Row(horizontalArrangement=Arrangement.spacedBy(7.dp)) {
                unitCategories.forEachIndexed { i, item ->
                    FilterChip(selected=categoryIndex==i,onClick={categoryIndex=i;fromIndex=0;toIndex=if(item.units.size>1)1 else 0},label={Text(if(fa)item.fa else item.en)})
                }
            }
        }
        Surface(color=surface, shape=RoundedCornerShape(20.dp)) {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) {
                Text(if(fa) "مقدار ورودی" else "Input value", color=muted, fontSize=12.sp)
                OutlinedTextField(value=input,onValueChange={input=it},modifier=Modifier.fillMaxWidth(),singleLine=true,label={Text(if(fa)"مقدار" else "Value")})
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                    Box(Modifier.weight(1f)) {
                        OutlinedButton(onClick={fromExpanded=true},modifier=Modifier.fillMaxWidth()) { Text(category.units[fromIndex].symbol) }
                        DropdownMenu(expanded=fromExpanded,onDismissRequest={fromExpanded=false}) {
                            category.units.forEachIndexed { i,u -> DropdownMenuItem(text={Text(u.symbol)},onClick={fromIndex=i;fromExpanded=false}) }
                        }
                    }
                    Text("→",color=Color(0xFF35B6FF),fontSize=24.sp,modifier=Modifier.padding(top=7.dp))
                    Box(Modifier.weight(1f)) {
                        OutlinedButton(onClick={toExpanded=true},modifier=Modifier.fillMaxWidth()) { Text(category.units[toIndex].symbol) }
                        DropdownMenu(expanded=toExpanded,onDismissRequest={toExpanded=false}) {
                            category.units.forEachIndexed { i,u -> DropdownMenuItem(text={Text(u.symbol)},onClick={toIndex=i;toExpanded=false}) }
                        }
                    }
                }
                HorizontalDivider(color=muted.copy(alpha=.2f))
                Text(if(fa)"نتیجه" else "Result",color=muted,fontSize=12.sp)
                Text(converted?.let(::formatConversion) ?: if(fa)"عدد معتبر وارد کن" else "Enter a valid number",color=Color(0xFF35B6FF),fontSize=29.sp,fontWeight=FontWeight.Bold)
                Text("${category.units[fromIndex].symbol}  →  ${category.units[toIndex].symbol}",color=muted,fontSize=11.sp)
                TextButton(onClick={val old=fromIndex;fromIndex=toIndex;toIndex=old}) { Text(if(fa)"جابه‌جایی واحدها" else "Swap units") }
            }
        }
        Surface(color=surface,shape=RoundedCornerShape(16.dp)) {
            Column(Modifier.padding(15.dp),verticalArrangement=Arrangement.spacedBy(5.dp)) {
                Text(if(fa)"نکته محاسباتی" else "Engineering note",color=fg,fontWeight=FontWeight.Bold)
                Text(if(fa)"در فشار، واحدهای گیج و مطلق یکسان نیستند. برای روابط ترمودینامیکی و گازها، فشار مطلق را به‌کار ببر." else "Gauge and absolute pressure are not interchangeable. Use absolute pressure in thermodynamic and gas-law equations.",color=muted,fontSize=12.sp)
            }
        }
    }
}
