package com.mechnos.app

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private const val FEEDBACK_EMAIL = "gamirreza483@gmail.com"

@Composable
fun AboutMechOSPage(fa:Boolean,fg:Color,muted:Color,surface:Color) {
    val context=LocalContext.current
    var message by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(if(fa)"پیشنهاد" else "Suggestion") }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(13.dp)) {
        Text(if(fa)"درباره MechOS" else "About MechOS",color=fg,fontSize=23.sp,fontWeight=FontWeight.Bold)
        Surface(color=surface,shape=RoundedCornerShape(20.dp)) {
            Column(Modifier.fillMaxWidth().padding(18.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Info,null,tint=Color(0xFF35B6FF),modifier=Modifier.size(32.dp))
                Text("MechOS",color=fg,fontSize=22.sp,fontWeight=FontWeight.Black)
                Text(if(fa)"جعبه‌ابزار و همراه آموزشی مهندسی مکانیک؛ برای مرور مفاهیم، تمرین حل مسئله و انجام محاسبات روزمره روی موبایل." else "A mobile engineering toolbox and study companion for reviewing concepts, practicing problems and everyday calculations.",color=muted,fontSize=13.sp,lineHeight=21.sp)
                HorizontalDivider(color=muted.copy(alpha=.25f))
                Text(if(fa)"سازنده" else "Created by",color=muted,fontSize=11.sp)
                Text(if(fa)"امیررضا" else "Amir Reza",color=fg,fontSize=19.sp,fontWeight=FontWeight.Bold)
                Text(if(fa)"دانشجوی مهندسی مکانیک" else "Mechanical Engineering Student",color=Color(0xFF35B6FF),fontSize=13.sp)
                Text("MechOS • Version 1.7",color=muted,fontSize=11.sp)
            }
        }
        Text(if(fa)"ارسال بازخورد" else "Send feedback",color=fg,fontSize=18.sp,fontWeight=FontWeight.Bold)
        Text(if(fa)"نظر، پیشنهاد یا گزارش خطا را بنویس. برنامه ایمیل را با گیرنده آماده باز می‌کند تا خودت ارسالش کنی." else "Write a suggestion or bug report. Your email app opens with the recipient and message ready for you to send.",color=muted,fontSize=12.sp,lineHeight=19.sp)
        OutlinedTextField(value=category,onValueChange={category=it},modifier=Modifier.fillMaxWidth(),singleLine=true,label={Text(if(fa)"موضوع بازخورد" else "Feedback category")})
        OutlinedTextField(value=message,onValueChange={message=it},modifier=Modifier.fillMaxWidth().heightIn(min=120.dp),label={Text(if(fa)"متن بازخورد" else "Your feedback")},minLines=4)
        Button(onClick={
            val body=(if(fa)"نوع: " else "Category: ")+category+"\n\n"+message+"\n\nMechOS 1.7"
            val intent=Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"+FEEDBACK_EMAIL)).apply {
                putExtra(Intent.EXTRA_SUBJECT,if(fa)"بازخورد MechOS" else "MechOS feedback")
                putExtra(Intent.EXTRA_TEXT,body)
            }
            runCatching { context.startActivity(Intent.createChooser(intent,if(fa)"ارسال بازخورد با ایمیل" else "Send feedback by email")) }
        },enabled=message.isNotBlank(),modifier=Modifier.fillMaxWidth().heightIn(min=50.dp)) {
            Icon(Icons.Default.Email,null); Spacer(Modifier.width(8.dp)); Text(if(fa)"ارسال به ایمیل سازنده" else "Email the developer")
        }
        Text(if(fa)"گیرنده: "+FEEDBACK_EMAIL else "Recipient: "+FEEDBACK_EMAIL,color=muted,fontSize=11.sp)
        Text(if(fa)"هیچ پیامی بدون تأیید تو ارسال نمی‌شود؛ ارسال نهایی را در برنامه ایمیل انجام می‌دهی." else "Nothing is sent without your approval; you complete the send action in your email app.",color=muted,fontSize=10.sp)
    }
}

private data class SatPoint(val t:Double,val p:Double,val vf:Double,val vg:Double,val hf:Double,val hfg:Double,val sf:Double,val sfg:Double)
private val waterSat=listOf(
    SatPoint(0.01,.6117,.001000,206.14,.01,2500.9,0.0000,9.1555),
    SatPoint(20.0,2.339,.001002,57.79,83.91,2454.1,.2965,8.3660),
    SatPoint(40.0,7.385,.001008,19.52,167.45,2406.0,.5724,7.6840),
    SatPoint(60.0,19.946,.001017,7.67,251.13,2358.0,.8313,7.0760),
    SatPoint(80.0,47.414,.001029,3.407,334.91,2308.0,1.0753,6.5020),
    SatPoint(100.0,101.325,.001043,1.673,419.04,2256.5,1.3072,5.9490),
    SatPoint(120.0,198.67,.001060,.8913,503.8,2202.1,1.5301,5.4300),
    SatPoint(140.0,361.5,.001080,.5078,589.1,2144.7,1.7395,4.9400),
    SatPoint(160.0,618.2,.001102,.3068,675.5,2083.6,1.9427,4.4800),
    SatPoint(180.0,1002.8,.001127,.1941,763.0,2015.3,2.1383,4.0300),
    SatPoint(200.0,1554.9,.001157,.1274,852.4,1940.7,2.3309,3.5900)
)

@Composable
fun ThermodynamicsReferencePage(fa:Boolean,fg:Color,muted:Color,surface:Color) {
    var tab by remember { mutableIntStateOf(0) }
    Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(10.dp)) {
        Text(if(fa)"مرجع ترمودینامیک" else "Thermodynamics reference",color=fg,fontSize=22.sp,fontWeight=FontWeight.Bold)
        Text(if(fa)"نمودارهای نواحی فازی، گنبد اشباع و راهنمای تحلیل حالت" else "Phase diagrams, saturation domes and state-analysis guide",color=muted,fontSize=11.sp)
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
            listOf(if(fa)"نمودارهای فازی" else "Phase diagrams",if(fa)"نمودارهای خواص" else "Property plots",if(fa)"راهنما" else "Guide").forEachIndexed { i,label ->
                FilterChip(selected=tab==i,onClick={tab=i},label={Text(label)})
            }
        }
        when(tab) {
            0 -> Column(Modifier.weight(1f).verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)) {
                listOf(
                    Triple("P-v",if(fa)"فشار–حجم ویژه؛ گنبد اشباع و خطوط دمای ثابت" else "Pressure–specific volume: saturation dome and isotherms",if(fa)"داخل گنبد: مخلوط مایع–بخار؛ چپ: مایع فشرده؛ راست: بخار فوق‌گرم." else "Inside the dome: liquid–vapor mixture; left: compressed liquid; right: superheated vapor."),
                    Triple("P-T",if(fa)"فشار–دما؛ مرزهای فازی و نقطه سه‌گانه و بحرانی" else "Pressure–temperature: phase boundaries, triple and critical points",if(fa)"نقطه سه‌گانه محل هم‌زیستی سه فاز و نقطه بحرانی انتهای منحنی تبخیر است." else "The triple point is where three phases coexist; the critical point terminates the vaporization curve."),
                    Triple("T-v",if(fa)"دما–حجم ویژه؛ خطوط فشار ثابت و ناحیه دوفازی" else "Temperature–specific volume: isobars and two-phase region",if(fa)"در ناحیه دوفازی، دما و فشار مستقل نیستند و در حالت اشباع به هم وابسته‌اند." else "In the two-phase region, temperature and pressure are not independent; they are linked by saturation.")
                ).forEach { (kind,title,desc) ->
                    Surface(color=surface,shape=RoundedCornerShape(16.dp)) {
                        Column(Modifier.fillMaxWidth().padding(12.dp),verticalArrangement=Arrangement.spacedBy(7.dp)) {
                            Text(title,color=fg,fontWeight=FontWeight.Bold,fontSize=13.sp)
                            ThermoSchematic(kind)
                            Text(desc,color=muted,fontSize=11.sp,lineHeight=17.sp)
                        }
                    }
                }
                Text(if(fa)"این شکل‌ها بازترسیم آموزشی از نمودارهای مرجع ارسالی هستند؛ مقیاس محورها و شکل منحنی‌ها شماتیک است." else "These are educational redrawings of the supplied reference diagrams; axes and curve shapes are schematic, not to scale.",color=muted,fontSize=10.sp)
            }
            1 -> Column(Modifier.weight(1f).verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)) {
                listOf("P-v","T-v","T-s","P-h").forEach { kind ->
                    Surface(color=surface,shape=RoundedCornerShape(16.dp)) {
                        Column(Modifier.fillMaxWidth().padding(12.dp),verticalArrangement=Arrangement.spacedBy(5.dp)) {
                            Text(kind+ " • "+(if(fa)"گنبد اشباع" else "Saturation dome"),color=fg,fontWeight=FontWeight.Bold)
                            ThermoSchematic(kind)
                            Text(if(fa)"نمای مفهومی و غیرمقیاس؛ برای محاسبات عددی از جدول خواص معتبر استفاده کن." else "Conceptual, not-to-scale plot; use validated property data for numerical calculations.",color=muted,fontSize=10.sp)
                        }
                    }
                }
            }
            else -> Column(Modifier.weight(1f).verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(10.dp)) {
                ThermoGuideCard(if(fa)"۱) مایع فشرده" else "1) Compressed liquid",if(fa)"اگر T کمتر از Tsat در فشار داده‌شده باشد، معمولاً مایع فشرده است. تقریب رایج: v≈vf(T) و u≈uf(T)، اما برای دقت بالا از جدول مایع فشرده استفاده کن." else "If T is below Tsat at the given pressure, the state is usually compressed liquid. Common approximation: v≈vf(T), u≈uf(T); use compressed-liquid data for higher accuracy.",surface,fg,muted)
                ThermoGuideCard(if(fa)"۲) مخلوط اشباع" else "2) Saturated mixture",if(fa)"بین مایع اشباع و بخار اشباع: y = yf + x·yfg برای v، u، h و s. کیفیت x = (y−yf)/yfg و فقط در ناحیه دوفازی معنا دارد." else "Between saturated liquid and vapor: y = yf + x·yfg for v, u, h and s. Quality x=(y−yf)/yfg is meaningful only in the two-phase region.",surface,fg,muted)
                ThermoGuideCard(if(fa)"۳) بخار فوق‌گرم" else "3) Superheated vapor",if(fa)"اگر T بیشتر از Tsat در فشار داده‌شده باشد، جدول بخار فوق‌گرم را در فشار ثابت پیدا کن و بین دماها درون‌یابی کن." else "If T exceeds Tsat at the given pressure, use the superheated table at fixed pressure and interpolate between temperatures.",surface,fg,muted)
                ThermoGuideCard(if(fa)"۴) نقطه سه‌گانه و بحرانی" else "4) Triple and critical points",if(fa)"نقطه سه‌گانه محل تعادل جامد، مایع و بخار است. در نقطه بحرانی تمایز مایع و بخار از بین می‌رود و انتهای گنبد اشباع قرار دارد." else "The triple point is the equilibrium of solid, liquid and vapor. At the critical point, liquid and vapor become indistinguishable and the saturation dome terminates.",surface,fg,muted)
                ThermoGuideCard(if(fa)"۵) گاز ایده‌آل" else "5) Ideal gas",if(fa)"برای گاز ایده‌آل: Pv=RT. در تغییر دما از h(T)، u(T)، Pr و vr استفاده کن؛ با فرض گرمای ویژه ثابت، Δh≈cpΔT و Δu≈cvΔT." else "For an ideal gas: Pv=RT. For temperature changes use h(T), u(T), Pr and vr; with constant specific heats, Δh≈cpΔT and Δu≈cvΔT.",surface,fg,muted)
                ThermoGuideCard(if(fa)"۶) قانون مساحت در نمودارها" else "6) Area interpretation",if(fa)"در T-s، برای فرایند برگشت‌پذیر q = ∫T ds؛ در P-v، کار مرزی w = ∫P dv. مساحت فقط با مقیاس و مسیر درست معنای کمی دارد." else "On T-s, reversible heat is q = ∫T ds; on P-v, boundary work is w = ∫P dv. Areas are quantitative only when axes and path are correctly scaled.",surface,fg,muted)
                Text(if(fa)"یادآوری: نمودارها آموزشی‌اند و جایگزین داده‌های عددی جدول‌های کامل بخار یا استانداردهای خواص نیستند." else "Note: these plots are educational and do not replace numerical steam tables or property standards.",color=Color(0xFFFFB86B),fontSize=11.sp)
            }
        }
    }
}

@Composable
private fun ThermoGuideCard(title:String,body:String,surface:Color,fg:Color,muted:Color) {
    Surface(color=surface,shape=RoundedCornerShape(14.dp)) { Column(Modifier.fillMaxWidth().padding(13.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) { Text(title,color=fg,fontWeight=FontWeight.Bold,fontSize=13.sp);Text(body,color=muted,fontSize=11.sp,lineHeight=18.sp) } }
}

@Composable
private fun ThermoSchematic(kind:String) {
    Canvas(Modifier.fillMaxWidth().height(165.dp)) {
        val l=42f;val r=size.width-12f;val top=10f;val b=size.height-26f
        val axis=Color(0xFF8FA8BD);val curve=Color(0xFF35B6FF)
        drawLine(axis,Offset(l,b),Offset(r,b),2f);drawLine(axis,Offset(l,b),Offset(l,top),2f)
        val dome=Path().apply{moveTo(l+(r-l)*.22f,b-(b-top)*.04f);cubicTo(l+(r-l)*.25f,b-(b-top)*.55f,l+(r-l)*.39f,b-(b-top)*.93f,l+(r-l)*.5f,b-(b-top)*.95f);cubicTo(l+(r-l)*.64f,b-(b-top)*.93f,l+(r-l)*.78f,b-(b-top)*.55f,l+(r-l)*.8f,b-(b-top)*.04f)}
        drawPath(dome,curve,style=Stroke(3f))
        val process=Path().apply{moveTo(l+(r-l)*.12f,b-(b-top)*.2f);cubicTo(l+(r-l)*.35f,b-(b-top)*.3f,l+(r-l)*.57f,b-(b-top)*.42f,l+(r-l)*.9f,b-(b-top)*.7f)}
        drawPath(process,Color(0xFFFFB86B),style=Stroke(2.5f))
    }
    Text(if(kind=="P-v")"P" else if(kind=="T-v")"T" else if(kind=="T-s")"T" else "P",color=Color(0xFF35B6FF),fontSize=10.sp)
    Text(if(kind=="P-v")"v" else if(kind=="T-v")"v" else if(kind=="T-s")"s" else "h",color=Color(0xFF35B6FF),fontSize=10.sp)
}
