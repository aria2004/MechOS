package com.mechnos.app

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
private data class UnitCategory(
    val fa: String,
    val en: String,
    val units: List<EngineeringUnit>,
    val temperature: Boolean = false
)

private val unitCategories = listOf(
    UnitCategory("طول", "Length", listOf(
        EngineeringUnit("m"), EngineeringUnit("mm", .001), EngineeringUnit("cm", .01),
        EngineeringUnit("km", 1000.0), EngineeringUnit("µm", 1e-6),
        EngineeringUnit("in", .0254), EngineeringUnit("ft", .3048),
        EngineeringUnit("yd", .9144), EngineeringUnit("mi", 1609.344),
        EngineeringUnit("nmi", 1852.0)
    )),
    UnitCategory("مساحت", "Area", listOf(
        EngineeringUnit("m²"), EngineeringUnit("mm²", 1e-6), EngineeringUnit("cm²", 1e-4),
        EngineeringUnit("km²", 1e6), EngineeringUnit("in²", .00064516),
        EngineeringUnit("ft²", .09290304), EngineeringUnit("yd²", .83612736),
        EngineeringUnit("acre", 4046.8564224), EngineeringUnit("ha", 10000.0)
    )),
    UnitCategory("حجم", "Volume", listOf(
        EngineeringUnit("m³"), EngineeringUnit("cm³", 1e-6), EngineeringUnit("L", .001),
        EngineeringUnit("mL", 1e-6), EngineeringUnit("in³", 1.6387064e-5),
        EngineeringUnit("ft³", .028316846592), EngineeringUnit("US gal", .003785411784),
        EngineeringUnit("UK gal", .00454609), EngineeringUnit("US fl oz", 2.95735295625e-5)
    )),
    UnitCategory("جرم", "Mass", listOf(
        EngineeringUnit("kg"), EngineeringUnit("g", .001), EngineeringUnit("mg", 1e-6),
        EngineeringUnit("tonne", 1000.0), EngineeringUnit("oz", .028349523125),
        EngineeringUnit("lb", .45359237), EngineeringUnit("stone", 6.35029318),
        EngineeringUnit("US ton", 907.18474), EngineeringUnit("UK ton", 1016.0469088)
    )),
    UnitCategory("نیرو", "Force", listOf(
        EngineeringUnit("N"), EngineeringUnit("kN", 1000.0), EngineeringUnit("MN", 1e6),
        EngineeringUnit("lbf", 4.4482216152605), EngineeringUnit("kgf", 9.80665),
        EngineeringUnit("dyn", 1e-5)
    )),
    UnitCategory("فشار", "Pressure", listOf(
        EngineeringUnit("Pa"), EngineeringUnit("kPa", 1000.0), EngineeringUnit("MPa", 1e6),
        EngineeringUnit("GPa", 1e9), EngineeringUnit("bar", 100000.0),
        EngineeringUnit("mbar", 100.0), EngineeringUnit("atm", 101325.0),
        EngineeringUnit("psi", 6894.757293168), EngineeringUnit("ksi", 6894757.293168),
        EngineeringUnit("mmHg", 133.322387415), EngineeringUnit("inHg", 3386.389)
    )),
    UnitCategory("انرژی", "Energy", listOf(
        EngineeringUnit("J"), EngineeringUnit("kJ", 1000.0), EngineeringUnit("MJ", 1e6),
        EngineeringUnit("cal", 4.184), EngineeringUnit("kcal", 4184.0),
        EngineeringUnit("Wh", 3600.0), EngineeringUnit("kWh", 3.6e6),
        EngineeringUnit("Btu", 1055.05585262), EngineeringUnit("ft·lbf", 1.3558179483)
    )),
    UnitCategory("توان", "Power", listOf(
        EngineeringUnit("W"), EngineeringUnit("kW", 1000.0), EngineeringUnit("MW", 1e6),
        EngineeringUnit("hp", 745.699871582), EngineeringUnit("BTU/h", .2930710702),
        EngineeringUnit("ft·lbf/s", 1.3558179483)
    )),
    UnitCategory("سرعت", "Speed", listOf(
        EngineeringUnit("m/s"), EngineeringUnit("km/h", 1.0 / 3.6),
        EngineeringUnit("mph", .44704), EngineeringUnit("ft/s", .3048),
        EngineeringUnit("knot", .5144444444)
    )),
    UnitCategory("شتاب", "Acceleration", listOf(
        EngineeringUnit("m/s²"), EngineeringUnit("ft/s²", .3048),
        EngineeringUnit("in/s²", .0254), EngineeringUnit("g₀", 9.80665)
    )),
    UnitCategory("گشتاور", "Torque", listOf(
        EngineeringUnit("N·m"), EngineeringUnit("N·mm", .001),
        EngineeringUnit("kN·m", 1000.0), EngineeringUnit("lbf·ft", 1.3558179483),
        EngineeringUnit("lbf·in", .112984829)
    )),
    UnitCategory("چگالی", "Density", listOf(
        EngineeringUnit("kg/m³"), EngineeringUnit("g/cm³", 1000.0),
        EngineeringUnit("kg/L", 1000.0), EngineeringUnit("lb/ft³", 16.01846337),
        EngineeringUnit("lb/in³", 27679.90471)
    )),
    UnitCategory("دبی حجمی", "Volumetric Flow", listOf(
        EngineeringUnit("m³/s"), EngineeringUnit("L/s", .001),
        EngineeringUnit("L/min", 1.0 / 60000.0), EngineeringUnit("m³/h", 1.0 / 3600.0),
        EngineeringUnit("US gpm", .003785411784 / 60.0),
        EngineeringUnit("ft³/min", .028316846592 / 60.0)
    )),
    UnitCategory("دما", "Temperature", listOf(
        EngineeringUnit("°C"), EngineeringUnit("°F"), EngineeringUnit("K")
    ), true),
    UnitCategory("زاویه", "Angle", listOf(
        EngineeringUnit("rad"), EngineeringUnit("deg", Math.PI / 180.0),
        EngineeringUnit("rev", 2.0 * Math.PI), EngineeringUnit("grad", Math.PI / 200.0)
    )),
    UnitCategory("دور و فرکانس", "Rotational Frequency", listOf(
        EngineeringUnit("Hz"), EngineeringUnit("kHz", 1000.0),
        EngineeringUnit("rpm", 1.0 / 60.0), EngineeringUnit("rad/s", 1.0 / (2.0 * Math.PI))
    ))
)

private fun toBase(value: Double, category: UnitCategory, index: Int): Double {
    if (!category.temperature) return value * category.units[index].factor
    return when (index) {
        0 -> value + 273.15
        1 -> (value - 32.0) * 5.0 / 9.0 + 273.15
        else -> value
    }
}

private fun fromBase(value: Double, category: UnitCategory, index: Int): Double {
    if (!category.temperature) return value / category.units[index].factor
    return when (index) {
        0 -> value - 273.15
        1 -> (value - 273.15) * 9.0 / 5.0 + 32.0
        else -> value
    }
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

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(if (fa) "تبدیل واحد مهندسی" else "Engineering Unit Converter",
            color = fg, fontSize = 23.sp, fontWeight = FontWeight.Bold)
        Text(if (fa) "بیش از ۱۵ گروه کمیت؛ متریک، امپریال و آمریکایی"
            else "15+ quantity groups; metric, imperial and US customary units",
            color = muted, fontSize = 12.sp)

        Column(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
            Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                unitCategories.forEachIndexed { i, item ->
                    FilterChip(
                        selected = categoryIndex == i,
                        onClick = {
                            categoryIndex = i
                            fromIndex = 0
                            toIndex = if (item.units.size > 1) 1 else 0
                        },
                        label = { Text(if (fa) item.fa else item.en) }
                    )
                }
            }
        }

        Surface(color = surface, shape = RoundedCornerShape(20.dp)) {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(if (fa) "مقدار ورودی" else "Input value", color = muted, fontSize = 12.sp)
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text(if (fa) "مقدار" else "Value") }
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(Modifier.weight(1f)) {
                        OutlinedButton(onClick = { fromExpanded = true }, modifier = Modifier.fillMaxWidth()) {
                            Text(category.units[fromIndex].symbol)
                        }
                        DropdownMenu(expanded = fromExpanded, onDismissRequest = { fromExpanded = false }) {
                            category.units.forEachIndexed { i, unit ->
                                DropdownMenuItem(text = { Text(unit.symbol) }, onClick = {
                                    fromIndex = i
                                    fromExpanded = false
                                })
                            }
                        }
                    }
                    Text("→", color = Color(0xFF35B6FF), fontSize = 24.sp, modifier = Modifier.padding(top = 7.dp))
                    Box(Modifier.weight(1f)) {
                        OutlinedButton(onClick = { toExpanded = true }, modifier = Modifier.fillMaxWidth()) {
                            Text(category.units[toIndex].symbol)
                        }
                        DropdownMenu(expanded = toExpanded, onDismissRequest = { toExpanded = false }) {
                            category.units.forEachIndexed { i, unit ->
                                DropdownMenuItem(text = { Text(unit.symbol) }, onClick = {
                                    toIndex = i
                                    toExpanded = false
                                })
                            }
                        }
                    }
                }
                HorizontalDivider(color = muted.copy(alpha = .2f))
                Text(if (fa) "نتیجه" else "Result", color = muted, fontSize = 12.sp)
                Text(
                    converted?.let(::formatConversion) ?: if (fa) "عدد معتبر وارد کن" else "Enter a valid number",
                    color = Color(0xFF35B6FF), fontSize = 29.sp, fontWeight = FontWeight.Bold
                )
                Text("${category.units[fromIndex].symbol}  →  ${category.units[toIndex].symbol}", color = muted, fontSize = 11.sp)
                TextButton(onClick = {
                    val old = fromIndex
                    fromIndex = toIndex
                    toIndex = old
                }) { Text(if (fa) "جابه‌جایی واحدها" else "Swap units") }
            }
        }

        Surface(color = surface, shape = RoundedCornerShape(16.dp)) {
            Column(Modifier.padding(15.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(if (fa) "نکته مهندسی" else "Engineering note", color = fg, fontWeight = FontWeight.Bold)
                Text(
                    if (fa) "واحدهای lb جرم، lbf نیرو و psi فشار هستند؛ این سه را با هم اشتباه نگیر. در ترمودینامیک معمولاً فشار مطلق لازم است، نه فشار گیج."
                    else "lb is mass, lbf is force, and psi is pressure. Do not mix them. Thermodynamic equations generally require absolute pressure, not gauge pressure.",
                    color = muted, fontSize = 12.sp
                )
            }
        }
    }
}
