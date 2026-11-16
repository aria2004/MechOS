package com.mechnos.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlin.math.PI

private data class WorkshopTool(val fa:String,val en:String,val formula:String)
private val workshopTools = listOf(
    WorkshopTool("تنش محوری","Axial stress","σ = F / A"),
    WorkshopTool("واکنش تکیه‌گاهی تیر","Beam reaction","RB = F × a / L"),
    WorkshopTool("انبساط حرارتی","Thermal expansion","ΔL = α × L × ΔT"),
    WorkshopTool("توان محور","Shaft power","P = T × 2πn / 60")
)

@Composable
fun EngineeringWorkshopPage(fa:Boolean, fg:Color, muted:Color, surface:Color) {
    var selected by remember { mutableIntStateOf(0) }
    var a by remember { mutableStateOf("1000") }
    var b by remember { mutableStateOf("100") }
    var c by remember { mutableStateOf("1") }
    var d by remember { mutableStateOf("20") }
    val x=a.replace(',','.').toDoubleOrNull() ?: 0.0
    val y=b.replace(',','.').toDoubleOrNull() ?: 0.0
    val z=c.replace(',','.').toDoubleOrNull() ?: 0.0
    val w=d.replace(',','.').toDoubleOrNull() ?: 0.0
    val result = when(selected) {
        0 -> if(y>0) x/y else Double.NaN
        1 -> if(y>0) x*z/y else Double.NaN
        2 -> x*1e-6*y*z
        else -> x*2*PI*y/60.0
    }
    val unit = when(selected) { 0 -> "MPa (N/mm²)"; 1 -> "N"; 2 -> "mm"; else -> "W" }
    val labels = when(selected) {
        0 -> listOf("نیرو (N)","سطح مقطع (mm²)","—","—")
        1 -> listOf("بار متمرکز (N)","طول تیر L (m)","فاصله بار از A، a (m)","—")
        2 -> listOf("ضریب انبساط α (µm/m·K)","طول اولیه (mm)","تغییر دما (K)","—")
        else -> listOf("گشتاور (N·m)","سرعت (rpm)","—","—")
    }
    val labelsEn = when(selected) {
        0 -> listOf("Force (N)","Area (mm²)","—","—")
        1 -> listOf("Point load (N)","Beam length L (m)","Load distance from A, a (m)","—")
        2 -> listOf("Expansion coefficient α (µm/m·K)","Original length (mm)","Temperature change (K)","—")
        else -> listOf("Torque (N·m)","Speed (rpm)","—","—")
    }
    Column(Modifier.fillMaxSize()) {
        Text(if(fa)"کارگاه محاسبات مهندسی" else "Engineering Calculation Workshop",color=fg,fontSize=22.sp,fontWeight=FontWeight.Bold)
        Text(if(fa)"چهار محاسبه کاربردی با نمایش رابطه و نتیجه لحظه‌ای" else "Four practical calculators with live results and formulas",color=muted,fontSize=12.sp)
        Spacer(Modifier.height(10.dp))
        LazyColumn(verticalArrangement=Arrangement.spacedBy(12.dp),contentPadding=PaddingValues(bottom=24.dp)) {
            item {
                Column(verticalArrangement=Arrangement.spacedBy(8.dp)) {
                    workshopTools.forEachIndexed { i, tool ->
                        FilterChip(selected=selected==i,onClick={selected=i},label={Text(if(fa)tool.fa else tool.en)},leadingIcon={if(selected==i) Icon(Icons.Default.Calculate,null)})
                    }
                }
            }
            item {
                Surface(color=surface,shape=RoundedCornerShape(18.dp)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)) {
                        Text(if(fa)workshopTools[selected].fa else workshopTools[selected].en,color=fg,fontSize=18.sp,fontWeight=FontWeight.Bold)
                        Text(workshopTools[selected].formula,color=Color(0xFF35B6FF),fontSize=20.sp,fontWeight=FontWeight.Bold)
                        @Composable fun field(label:String,value:String,onChange:(String)->Unit) {
                            OutlinedTextField(value=value,onValueChange=onChange,label={Text(label)},modifier=Modifier.fillMaxWidth(),singleLine=true,keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal))
                        }
                        field(if(fa)labels[0] else labelsEn[0],a){a=it}
                        field(if(fa)labels[1] else labelsEn[1],b){b=it}
                        if(selected==1 || selected==2) field(if(fa)labels[2] else labelsEn[2],c){c=it}
                        HorizontalDivider()
                        Text(if(fa)"نتیجه" else "RESULT",color=muted,fontSize=11.sp,fontWeight=FontWeight.Bold)
                        Text(if(result.isFinite()) String.format(Locale.US,"%.5g %s",result,unit) else if(fa)"ورودی‌ها را بررسی کن" else "Check input values",color=if(result.isFinite()) Color(0xFF36C98F) else Color(0xFFFF8A80),fontSize=24.sp,fontWeight=FontWeight.Black)
                        Text(when(selected) {
                            0 -> if(fa)"فرض: نیرو بر حسب نیوتن و سطح بر حسب میلی‌متر مربع؛ نتیجه مستقیماً MPa است." else "Assumes force in N and area in mm²; result is MPa."
                            1 -> if(fa)"تیر ساده با بار متمرکز؛ واکنش تکیه‌گاه B از تعادل گشتاورها." else "Simply supported beam with one point load; reaction at B from moment equilibrium."
                            2 -> if(fa)"α بر حسب میکرومتر بر متر-کلوین وارد می‌شود؛ طول بر حسب میلی‌متر." else "Enter α in microstrain per kelvin and length in mm."
                            else -> if(fa)"سرعت دورانی بر حسب دور بر دقیقه و گشتاور بر حسب نیوتن‌متر." else "Rotational speed in rpm and torque in N·m."
                        },color=muted,fontSize=11.sp)
                    }
                }
            }
        }
    }
}

