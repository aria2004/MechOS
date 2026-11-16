package com.mechnos.app

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import net.objecthunter.exp4j.ExpressionBuilder
import java.util.Locale
import kotlin.math.*

@Composable
fun ToolsHubPage(fa:Boolean,fg:Color,muted:Color,surface:Color,go:(Int)->Unit) {
    val entries=listOf(
        Triple(Icons.Default.Functions,if(fa)"بانک فرمول‌ها" else "Formula library",3),
        Triple(Icons.Default.SwapHoriz,if(fa)"تبدیل واحد" else "Unit converter",4),
        Triple(Icons.Default.Build,if(fa)"کارگاه مهندسی" else "Engineering workshop",7),
        Triple(Icons.Default.PrecisionManufacturing,if(fa)"ماشین‌کاری" else "Machining",13),
        Triple(Icons.Default.Inventory2,if(fa)"خواص مواد" else "Material database",9),
        Triple(Icons.Default.ShowChart,if(fa)"رسم نمودار" else "Graph lab",10),
        Triple(Icons.Default.DocumentScanner,if(fa)"اسکن فرمول" else "Formula scanner",11),
        Triple(Icons.Default.PictureAsPdf,if(fa)"نقشه و PDF" else "Drawing viewer",12)
    )
    Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        Text(if(fa)"جعبه‌ابزار مهندسی" else "Engineering toolbox",color=fg,fontSize=23.sp,fontWeight=FontWeight.Bold)
        Text(if(fa)"ابزارهای سبک و قابل استفاده روی موبایل" else "Practical tools designed for mobile",color=muted,fontSize=12.sp)
        LazyVerticalGrid(columns=GridCells.Fixed(2),modifier=Modifier.fillMaxSize(),contentPadding=PaddingValues(bottom=20.dp),horizontalArrangement=Arrangement.spacedBy(10.dp),verticalArrangement=Arrangement.spacedBy(10.dp)) {
            items(entries) { item ->
                Surface(color=surface,shape=RoundedCornerShape(18.dp),modifier=Modifier.fillMaxWidth().height(116.dp).clickable{go(item.third)}) {
                    Column(Modifier.fillMaxSize().padding(14.dp),verticalArrangement=Arrangement.SpaceBetween) {
                        Icon(item.first,null,tint=Color(0xFF35B6FF),modifier=Modifier.size(27.dp))
                        Text(item.second,color=fg,fontWeight=FontWeight.SemiBold,fontSize=14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun MoreHubPage(fa:Boolean,fg:Color,muted:Color,surface:Color,go:(Int)->Unit) {
    val entries=listOf(
        Triple(Icons.Default.School,if(fa)"برنامه مطالعه و مرور" else "Study planner",14),
        Triple(Icons.Default.Quiz,if(fa)"آزمون و تمرین" else "Quiz & practice",6),
        Triple(Icons.Default.Settings,if(fa)"تنظیمات و ظاهر" else "Settings",5),
        Triple(Icons.Default.Public,if(fa)"منابع آنلاین" else "Online resources",8),
        Triple(Icons.Default.Thermostat,if(fa)"نمودارها و جداول ترمودینامیک" else "Thermodynamics charts & tables",18),
        Triple(Icons.Default.Info,if(fa)"درباره MechOS و بازخورد" else "About MechOS & feedback",17)
    )
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        Text(if(fa)"بیشتر" else "More",color=fg,fontSize=23.sp,fontWeight=FontWeight.Bold)
        Text(if(fa)"مطالعه، شخصی‌سازی و منابع" else "Learning, preferences and references",color=muted,fontSize=12.sp)
        entries.forEach { item ->
            Surface(color=surface,shape=RoundedCornerShape(16.dp),modifier=Modifier.fillMaxWidth().clickable{go(item.third)}) {
                Row(Modifier.padding(17.dp),verticalAlignment=Alignment.CenterVertically) {
                    Icon(item.first,null,tint=Color(0xFF35B6FF),modifier=Modifier.size(27.dp))
                    Spacer(Modifier.width(14.dp))
                    Text(item.second,color=fg,fontWeight=FontWeight.SemiBold,modifier=Modifier.weight(1f))
                    Icon(Icons.Default.ChevronRight,null,tint=muted)
                }
            }
        }
        Text(if(fa)"محاسبات ابزار کمکی‌اند؛ برای طراحی نهایی، استاندارد و ضرایب اطمینان را بررسی کن." else "Calculations are aids. Verify standards and safety factors before final design.",color=muted,fontSize=11.sp)
    }
}

private data class Mat(val fa:String,val en:String,val group:String,val rho:Double,val e:Double,val sy:Double,val su:Double,val note:String)
private val matData=listOf(
    Mat("فولاد ساختمانی S235","Structural steel S235","Steel",7850.0,200.0,235.0,360.0,"مقادیر اسمی؛ وابسته به استاندارد و ضخامت"),
    Mat("فولاد S355","Structural steel S355","Steel",7850.0,210.0,355.0,470.0,"خواص واقعی به گرید و ضخامت وابسته است"),
    Mat("فولاد زنگ‌نزن 304","Stainless steel 304","Steel",8000.0,193.0,215.0,505.0,"گرید آستنیتی؛ تنش تسلیم تقریبی"),
    Mat("چدن خاکستری","Gray cast iron","Cast iron",7200.0,110.0,150.0,200.0,"رفتار ترد؛ خواص به گرید وابسته است"),
    Mat("آلومینیوم 6061-T6","Aluminum 6061-T6","Aluminum",2700.0,68.9,276.0,310.0,"آلیاژ عملیات حرارتی‌شده"),
    Mat("آلومینیوم 7075-T6","Aluminum 7075-T6","Aluminum",2810.0,71.7,503.0,572.0,"استحکام بالا؛ خوردگی‌پذیری متفاوت"),
    Mat("مس خالص","Copper","Non-ferrous",8960.0,117.0,70.0,220.0,"وابسته به خلوص و حالت کارسرد"),
    Mat("برنج CuZn37","Brass CuZn37","Non-ferrous",8470.0,100.0,120.0,340.0,"مقادیر تقریبی"),
    Mat("تیتانیوم Ti-6Al-4V","Titanium Ti-6Al-4V","Titanium",4430.0,114.0,880.0,950.0,"آلیاژ متداول هوافضا"),
    Mat("پلی‌اتیلن HDPE","HDPE","Polymer",950.0,0.8,25.0,30.0,"خواص تابع دما و نرخ بارگذاری")
)

@Composable
fun MaterialDatabasePage(fa:Boolean,fg:Color,muted:Color,surface:Color) {
    var query by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("All") }
    val groups=listOf("All","Steel","Aluminum","Non-ferrous","Cast iron","Titanium","Polymer")
    val filtered=matData.filter { (group=="All" || it.group==group) && (query.isBlank() || it.fa.contains(query,true) || it.en.contains(query,true) || it.group.contains(query,true)) }
    Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(9.dp)) {
        Text(if(fa)"بانک خواص مواد" else "Material properties",color=fg,fontSize=22.sp,fontWeight=FontWeight.Bold)
        Text(if(fa)"مقادیر مرجع تقریبی؛ واحدها در هر کارت مشخص شده‌اند." else "Approximate reference values; units are shown on each card.",color=muted,fontSize=11.sp)
        OutlinedTextField(query,{query=it},modifier=Modifier.fillMaxWidth(),singleLine=true,placeholder={Text(if(fa)"جست‌وجوی ماده..." else "Search materials...")},leadingIcon={Icon(Icons.Default.Search,null)})
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
            groups.forEach { g -> FilterChip(selected=group==g,onClick={group=g},label={Text(if(fa && g=="All")"همه" else g)}) }
        }
        LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp),contentPadding=PaddingValues(bottom=24.dp)) {
            items(filtered) { m ->
                Surface(color=surface,shape=RoundedCornerShape(16.dp)) {
                    Column(Modifier.fillMaxWidth().padding(14.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) {
                        Text(if(fa)m.fa else m.en,color=fg,fontWeight=FontWeight.Bold,fontSize=15.sp)
                        Text(m.group,color=Color(0xFF35B6FF),fontSize=10.sp)
                        ResultLine(if(fa)"چگالی" else "Density",String.format(Locale.US,"%.0f kg/m³",m.rho),fg,muted)
                        ResultLine("Young's modulus",String.format(Locale.US,"%.1f GPa",m.e),fg,muted)
                        ResultLine(if(fa)"تنش تسلیم" else "Yield strength",String.format(Locale.US,"%.0f MPa",m.sy),fg,muted)
                        ResultLine(if(fa)"استحکام کششی" else "Tensile strength",String.format(Locale.US,"%.0f MPa",m.su),fg,muted)
                        Text(if(fa)m.note else "Typical indicative values; verify exact grade, temper and product standard.",color=muted,fontSize=10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun MachiningPage(fa:Boolean,fg:Color,muted:Color,surface:Color) {
    var speed by remember { mutableStateOf("120") }
    var diameter by remember { mutableStateOf("10") }
    var feed by remember { mutableStateOf("0.12") }
    var teeth by remember { mutableStateOf("4") }
    var force by remember { mutableStateOf("500") }
    val vc=speed.toDoubleOrNull() ?: 0.0
    val d=diameter.toDoubleOrNull() ?: 0.0
    val f=feed.toDoubleOrNull() ?: 0.0
    val z=teeth.toDoubleOrNull() ?: 0.0
    val fc=force.toDoubleOrNull() ?: 0.0
    val rpm=if(d>0)1000*vc/(PI*d) else 0.0
    val feedRate=f*z*rpm
    val power=fc*vc/60000.0
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(10.dp)) {
        Text(if(fa)"محاسبات ماشین‌کاری" else "Machining calculator",color=fg,fontSize=22.sp,fontWeight=FontWeight.Bold)
        Text(if(fa)"سرعت برشی، دور اسپیندل، پیشروی و توان تقریبی" else "Cutting speed, spindle RPM, feed rate and estimated power",color=muted,fontSize=12.sp)
        @Composable fun field(label:String,value:String,change:(String)->Unit) { OutlinedTextField(value,change,modifier=Modifier.fillMaxWidth(),singleLine=true,label={Text(label)},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal)) }
        field(if(fa)"سرعت برشی Vc (m/min)" else "Cutting speed Vc (m/min)",speed){speed=it}
        field(if(fa)"قطر ابزار D (mm)" else "Tool diameter D (mm)",diameter){diameter=it}
        field(if(fa)"پیشروی هر دندانه fz (mm/tooth)" else "Feed per tooth fz (mm/tooth)",feed){feed=it}
        field(if(fa)"تعداد دندانه z" else "Number of teeth z",teeth){teeth=it}
        field(if(fa)"نیروی برش Fc (N) برای تخمین توان" else "Cutting force Fc (N) for power estimate",force){force=it}
        Surface(color=surface,shape=RoundedCornerShape(18.dp)) {
            Column(Modifier.fillMaxWidth().padding(16.dp),verticalArrangement=Arrangement.spacedBy(9.dp)) {
                Text(if(fa)"نتایج" else "Results",color=fg,fontWeight=FontWeight.Bold)
                ResultLine(if(fa)"دور اسپیندل" else "Spindle speed",String.format(Locale.US,"%.0f rpm",rpm),fg,muted)
                ResultLine(if(fa)"سرعت پیشروی" else "Feed rate",String.format(Locale.US,"%.1f mm/min",feedRate),fg,muted)
                ResultLine(if(fa)"توان برشی تقریبی" else "Estimated cutting power",String.format(Locale.US,"%.3f kW",power),fg,muted)
            }
        }
        Text(if(fa)"توان تخمینی به نیروی برش وابسته است؛ جنس قطعه و توصیه سازنده ابزار را بررسی کن." else "Power is an estimate based on cutting force. Check workpiece material and tooling recommendations.",color=muted,fontSize=11.sp)
    }
}

@Composable
private fun ResultLine(label:String,value:String,fg:Color,muted:Color) {
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) { Text(label,color=muted,fontSize=12.sp); Text(value,color=fg,fontWeight=FontWeight.Bold,fontSize=13.sp) }
}

@Composable
fun GraphLabPage(fa:Boolean,fg:Color,muted:Color,surface:Color) {
    var equation by remember { mutableStateOf("sin(x)") }
    var minText by remember { mutableStateOf("-10") }
    var maxText by remember { mutableStateOf("10") }
    var radians by remember { mutableStateOf(true) }
    val min=minText.toDoubleOrNull() ?: -10.0
    val max=maxText.toDoubleOrNull() ?: 10.0
    val points=remember(equation,min,max,radians) {
        if(max<=min) emptyList() else runCatching {
            val expr=ExpressionBuilder(equation).variables("x").build()
            (0..240).map { i ->
                val x=min+(max-min)*i/240.0
                val input=if(radians)x else Math.toRadians(x)
                x to expr.setVariable("x",input).evaluate()
            }.filter { it.second.isFinite() && abs(it.second)<1e9 }
        }.getOrElse { emptyList() }
    }
    val yMin=points.minOfOrNull{it.second} ?: -1.0
    val yMax=points.maxOfOrNull{it.second} ?: 1.0
    val ySpan=(yMax-yMin).takeIf{it>1e-9} ?: 2.0
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(10.dp)) {
        Text(if(fa)"آزمایشگاه نمودار" else "Graph lab",color=fg,fontSize=22.sp,fontWeight=FontWeight.Bold)
        Text(if(fa)"تابع را با متغیر x وارد کن؛ مانند sin(x)، x^2 یا 2*x+1" else "Enter a function of x, e.g. sin(x), x^2 or 2*x+1",color=muted,fontSize=11.sp)
        OutlinedTextField(equation,{equation=it},modifier=Modifier.fillMaxWidth(),singleLine=true,label={Text("y = f(x)")})
        Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(minText,{minText=it},modifier=Modifier.weight(1f),singleLine=true,label={Text("x min")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal))
            OutlinedTextField(maxText,{maxText=it},modifier=Modifier.weight(1f),singleLine=true,label={Text("x max")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal))
        }
        Row(verticalAlignment=Alignment.CenterVertically) { Switch(checked=radians,onCheckedChange={radians=it}); Spacer(Modifier.width(8.dp)); Text(if(fa)"ورودی مثلثاتی بر حسب رادیان" else "Trigonometric input in radians",color=muted,fontSize=12.sp) }
        Surface(color=surface,shape=RoundedCornerShape(18.dp)) {
            if(points.size<2) Text(if(fa)"تابع یا بازه معتبر نیست." else "Invalid function or range.",color=Color(0xFFFF8A80),modifier=Modifier.padding(20.dp))
            else Canvas(Modifier.fillMaxWidth().height(240.dp).padding(12.dp)) {
                val left=24f; val right=size.width-8f; val top=8f; val bottom=size.height-22f
                val x0=left+(((0.0-min)/(max-min)).toFloat())*(right-left)
                val y0=bottom-(((0.0-yMin)/ySpan).toFloat())*(bottom-top)
                drawLine(Color.Gray.copy(alpha=.5f),androidx.compose.ui.geometry.Offset(left,y0.coerceIn(top,bottom)),androidx.compose.ui.geometry.Offset(right,y0.coerceIn(top,bottom)),strokeWidth=1f)
                drawLine(Color.Gray.copy(alpha=.5f),androidx.compose.ui.geometry.Offset(x0.coerceIn(left,right),top),androidx.compose.ui.geometry.Offset(x0.coerceIn(left,right),bottom),strokeWidth=1f)
                val path=Path()
                points.forEachIndexed { i,p ->
                    val px=left+(((p.first-min)/(max-min)).toFloat())*(right-left)
                    val py=bottom-(((p.second-yMin)/ySpan).toFloat())*(bottom-top)
                    if(i==0)path.moveTo(px.toFloat(),py.toFloat()) else path.lineTo(px.toFloat(),py.toFloat())
                }
                drawPath(path,Color(0xFF35B6FF),style=Stroke(width=3f))
            }
        }
        if(points.size>=2) {
            val mid=points[points.size/2]
            Surface(color=surface,shape=RoundedCornerShape(14.dp)) {
                Column(Modifier.fillMaxWidth().padding(14.dp),verticalArrangement=Arrangement.spacedBy(4.dp)) {
                    Text(if(fa)"تحلیل نمونه‌برداری" else "Sample analysis",color=fg,fontWeight=FontWeight.Bold)
                    Text("y min ≈ "+String.format(Locale.US,"%.4g",yMin)+"     y max ≈ "+String.format(Locale.US,"%.4g",yMax),color=muted,fontSize=12.sp)
                    Text("At x="+String.format(Locale.US,"%.4g",mid.first)+": y≈"+String.format(Locale.US,"%.4g",mid.second),color=Color(0xFF35B6FF),fontSize=12.sp)
                }
            }
        }
        Text(if(fa)"توابع پشتیبانی‌شده: sin, cos, tan, sqrt, log, ln و عملگرهای پایه." else "Supported functions: sin, cos, tan, sqrt, log, ln and basic operators.",color=muted,fontSize=10.sp)
    }
}

@Composable
fun FormulaScannerPage(fa:Boolean,fg:Color,muted:Color,surface:Color) {
    val context=LocalContext.current
    var extracted by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(if(fa)"یک تصویر از گالری یا فایل‌ها انتخاب کن." else "Choose an image from gallery or files.") }
    var busy by remember { mutableStateOf(false) }
    val launcher=rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri:Uri? ->
        if(uri!=null) {
            busy=true
            status=if(fa)"در حال استخراج متن..." else "Recognizing text..."
            try {
                val image=InputImage.fromFilePath(context,uri)
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS).process(image)
                    .addOnSuccessListener { result -> extracted=result.text; status=if(result.text.isBlank()) (if(fa)"متنی پیدا نشد" else "No text found") else (if(fa)"استخراج انجام شد؛ فرمول‌ها را بررسی کن" else "OCR complete — verify equations"); busy=false }
                    .addOnFailureListener { status=it.localizedMessage ?: "OCR failed"; busy=false }
            } catch(e:Exception) { status=e.localizedMessage ?: "Unable to read image"; busy=false }
        }
    }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        Text(if(fa)"اسکن فرمول و متن" else "Formula & text scanner",color=fg,fontSize=22.sp,fontWeight=FontWeight.Bold)
        Text(if(fa)"تصویر جزوه یا تخته را انتخاب کن تا متن آن با OCR استخراج شود." else "Select a page or board photo to extract text using on-device OCR.",color=muted,fontSize=12.sp)
        Button(onClick={launcher.launch(arrayOf("image/*"))},modifier=Modifier.fillMaxWidth(),enabled=!busy) { Icon(Icons.Default.AddPhotoAlternate,null); Spacer(Modifier.width(8.dp)); Text(if(fa)"انتخاب تصویر" else "Choose image") }
        if(busy) LinearProgressIndicator(Modifier.fillMaxWidth())
        Text(status,color=muted,fontSize=12.sp)
        if(extracted.isNotBlank()) Surface(color=surface,shape=RoundedCornerShape(16.dp)) {
            Column(Modifier.fillMaxWidth().padding(14.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                Text(if(fa)"متن استخراج‌شده" else "Extracted text",color=fg,fontWeight=FontWeight.Bold)
                Text(extracted,color=fg,fontSize=13.sp)
                OutlinedButton(onClick={val cm=context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager; cm.setPrimaryClip(android.content.ClipData.newPlainText("MechOS OCR",extracted))},modifier=Modifier.fillMaxWidth()) { Icon(Icons.Default.ContentCopy,null); Spacer(Modifier.width(6.dp)); Text(if(fa)"کپی متن" else "Copy text") }
            }
        }
        Text(if(fa)"نمادهای ریاضی، توان‌ها و زیرنویس‌ها ممکن است اشتباه خوانده شوند؛ حتماً با تصویر اصلی تطبیق بده." else "OCR may misread mathematical symbols, exponents and subscripts. Verify against the original image.",color=muted,fontSize=11.sp)
    }
}

@Composable
fun DrawingViewerPage(fa:Boolean,fg:Color,muted:Color,surface:Color) {
    val context=LocalContext.current
    var chosen by remember { mutableStateOf("") }
    var actual by remember { mutableStateOf("100") }
    var drawing by remember { mutableStateOf("50") }
    val av=actual.toDoubleOrNull() ?: 0.0
    val dv=drawing.toDoubleOrNull() ?: 0.0
    val scale=if(dv>0)av/dv else 0.0
    val openFile=rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri:Uri? ->
        if(uri!=null) {
            chosen=uri.lastPathSegment ?: "Selected document"
            val type=context.contentResolver.getType(uri) ?: if(chosen.endsWith(".pdf",true))"application/pdf" else "image/*"
            val intent=Intent(Intent.ACTION_VIEW).apply { setDataAndType(uri,type); addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) }
            runCatching { context.startActivity(intent) }
        }
    }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        Text(if(fa)"مشاهده‌گر نقشه و PDF" else "Drawing & PDF viewer",color=fg,fontSize=22.sp,fontWeight=FontWeight.Bold)
        Text(if(fa)"فایل با نمایشگر PDF یا تصویر دستگاه باز می‌شود." else "The file opens in your device's PDF or image viewer.",color=muted,fontSize=12.sp)
        Button(onClick={openFile.launch(arrayOf("application/pdf","image/*"))},modifier=Modifier.fillMaxWidth()) { Icon(Icons.Default.FolderOpen,null); Spacer(Modifier.width(8.dp)); Text(if(fa)"باز کردن نقشه یا PDF" else "Open drawing or PDF") }
        if(chosen.isNotBlank()) Text((if(fa)"فایل انتخاب‌شده: " else "Selected: ")+chosen,color=fg,fontSize=12.sp)
        Surface(color=surface,shape=RoundedCornerShape(18.dp)) {
            Column(Modifier.fillMaxWidth().padding(15.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                Text(if(fa)"محاسبه مقیاس از روی دو طول" else "Scale from two known lengths",color=fg,fontWeight=FontWeight.Bold)
                OutlinedTextField(actual,{actual=it},modifier=Modifier.fillMaxWidth(),label={Text(if(fa)"طول واقعی (mm)" else "Actual length (mm)")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal),singleLine=true)
                OutlinedTextField(drawing,{drawing=it},modifier=Modifier.fillMaxWidth(),label={Text(if(fa)"طول روی نقشه (mm)" else "Measured drawing length (mm)")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal),singleLine=true)
                Text(if(scale>0)String.format(Locale.US,"1 : %.3f",1/scale) else "—",color=Color(0xFF35B6FF),fontSize=24.sp,fontWeight=FontWeight.Bold)
                Text(if(fa)"این ابزار مقیاس را محاسبه می‌کند؛ اندازه‌گذاری مستقیم روی PDF در این نسخه نیست." else "This calculates scale; direct PDF markup is not included in this version.",color=muted,fontSize=10.sp)
            }
        }
    }
}

@Composable
fun StudyPlannerPage(fa:Boolean,fg:Color,muted:Color,surface:Color) {
    val tasks=if(fa)listOf("مرور مفاهیم درس امروز","حل ۳ مسئله عددی","مرور فرمول‌های مهم","ثبت اشکال‌ها و سؤال‌ها","آزمون کوتاه پایان مطالعه") else listOf("Review today's concepts","Solve 3 numerical problems","Review key formulas","Record doubts and questions","Take a short review quiz")
    val done=remember { mutableStateListOf(false,false,false,false,false) }
    val count=done.count{it}
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        Text(if(fa)"برنامه مطالعه مهندسی" else "Engineering study planner",color=fg,fontSize=22.sp,fontWeight=FontWeight.Bold)
        Text(if(fa)"یک چرخه کوتاه و قابل انجام برای هر جلسه مطالعه" else "A compact, repeatable cycle for each study session",color=muted,fontSize=12.sp)
        Surface(color=surface,shape=RoundedCornerShape(18.dp)) {
            Column(Modifier.fillMaxWidth().padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                Text(if(fa)"پیشرفت این جلسه" else "Session progress",color=fg,fontWeight=FontWeight.Bold)
                Text(count.toString()+" / 5",color=Color(0xFF35B6FF),fontSize=27.sp,fontWeight=FontWeight.Black)
                LinearProgressIndicator(progress={count/5f},modifier=Modifier.fillMaxWidth())
            }
        }
        tasks.forEachIndexed { i,task ->
            Surface(color=surface,shape=RoundedCornerShape(14.dp)) {
                Row(Modifier.fillMaxWidth().clickable{done[i]=!done[i]}.padding(14.dp),verticalAlignment=Alignment.CenterVertically) {
                    Checkbox(checked=done[i],onCheckedChange={done[i]=it})
                    Spacer(Modifier.width(8.dp)); Text(task,color=fg,fontSize=13.sp)
                }
            }
        }
        Text(if(fa)"رمز یادسپاری: «بخوان، حل کن، بازیابی کن»؛ فقط دوباره‌خوانی نکن، پاسخ را بدون نگاه به جزوه از حافظه بیرون بکش." else "Memory loop: Read, solve, retrieve. Recall the answer without looking at your notes.",color=muted,fontSize=11.sp)
    }
}
