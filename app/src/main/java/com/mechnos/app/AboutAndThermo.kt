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
                Text("MechOS • Version 1.8",color=muted,fontSize=11.sp)
            }
        }
        Text(if(fa)"ارسال بازخورد" else "Send feedback",color=fg,fontSize=18.sp,fontWeight=FontWeight.Bold)
        Text(if(fa)"نظر، پیشنهاد یا گزارش خطا را بنویس. برنامه ایمیل را با گیرنده آماده باز می‌کند تا خودت ارسالش کنی." else "Write a suggestion or bug report. Your email app opens with the recipient and message ready for you to send.",color=muted,fontSize=12.sp,lineHeight=19.sp)
        OutlinedTextField(value=category,onValueChange={category=it},modifier=Modifier.fillMaxWidth(),singleLine=true,label={Text(if(fa)"موضوع بازخورد" else "Feedback category")})
        OutlinedTextField(value=message,onValueChange={message=it},modifier=Modifier.fillMaxWidth().heightIn(min=120.dp),label={Text(if(fa)"متن بازخورد" else "Your feedback")},minLines=4)
        Button(onClick={
            val body=(if(fa)"نوع: " else "Category: ")+category+"\n\n"+message+"\n\nMechOS 1.8"
            val intent=Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:"+FEEDBACK_EMAIL)).apply {
                putExtra(Intent.EXTRA_SUBJECT,if(fa)"بازخورد MechOS" else "MechOS feedback")
                putExtra(Intent.EXTRA_TEXT,body)
            }
            runCatching { context.startActivity(Intent.createChooser(intent,if(fa)"ارسال بازخورد با ایمیل" else "Send feedback by email")) }
        },enabled=message.isNotBlank(),modifier=Modifier.fillMaxWidth().heightIn(min=50.dp)) {
            Icon(Icons.Default.Email,null); Spacer(Modifier.width(8.dp)); Text(if(fa)"ارسال به ایمیل سازنده" else "Email the developer")
        }
        Text(if(fa)"گیرنده: "+FEEDBACK_EMAIL else "Recipient: "+FEEDBACK_EMAIL,color=muted,fontSize=11.sp)
        Text(if(fa)"پیشنهادها و گزارش خطاها برای بهبود نسخه‌های بعدی MechOS استفاده می‌شوند." else "Suggestions and bug reports help improve future MechOS versions.",color=muted,fontSize=10.sp)
    }
}

@Composable
fun ThermodynamicsReferencePage(fa:Boolean,fg:Color,muted:Color,surface:Color) {
    var tab by remember { mutableIntStateOf(0) }
    Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(10.dp)) {
        Text(if(fa)"مرجع ترمودینامیک" else "Thermodynamics reference",color=fg,fontSize=22.sp,fontWeight=FontWeight.Bold)
        Text(if(fa)"نمودارهای مرجع ارسالی: نواحی فازی، گنبد اشباع و مسیر فرایندها" else "Reference diagrams: phase regions, saturation dome and process paths",color=muted,fontSize=11.sp)
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
            listOf(if(fa)"نمودارهای فازی" else "Phase diagrams",if(fa)"نمودارهای خواص" else "Property plots",if(fa)"راهنما" else "Guide").forEachIndexed { i,label ->
                FilterChip(selected=tab==i,onClick={tab=i},label={Text(label)})
            }
        }
        when(tab) {
            0 -> Column(Modifier.weight(1f).verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)) {
                listOf(
                    Triple("P-v",if(fa)"نمودار فشار–حجم ویژه و خطوط دمای ثابت" else "Pressure–specific volume and isotherms",if(fa)"زیر گنبد، ناحیه مخلوط اشباع قرار دارد؛ سمت چپ مایع فشرده و سمت راست بخار فوق‌گرم است. رأس گنبد نقطه بحرانی است." else "The saturation mixture lies under the dome; compressed liquid is on the left and superheated vapor on the right. The dome apex is the critical point."),
                    Triple("P-T",if(fa)"نمودار فشار–دما و مرزهای فازی" else "Pressure–temperature phase-boundary diagram",if(fa)"نقطه سه‌گانه محل هم‌زیستی جامد، مایع و بخار است؛ منحنی تبخیر در نقطه بحرانی پایان می‌یابد." else "The triple point is where solid, liquid and vapor coexist; the vaporization curve ends at the critical point."),
                    Triple("T-v",if(fa)"نمودار دما–حجم ویژه و مسیرهای فرایندی" else "Temperature–specific volume and process paths",if(fa)"خطوط افقی در ناحیه دوفازی، تغییر حجم در حالت اشباع را نشان می‌دهند؛ مسیر واقعی فرایند را با توجه به ثابت بودن فشار یا دما دنبال کن." else "Horizontal paths through the two-phase region illustrate volume change during saturation; identify whether pressure or temperature is held constant.")
                ).forEach { (kind,title,desc) ->
                    Surface(color=surface,shape=RoundedCornerShape(16.dp)) {
                        Column(Modifier.fillMaxWidth().padding(12.dp),verticalArrangement=Arrangement.spacedBy(7.dp)) {
                            Text(title,color=fg,fontWeight=FontWeight.Bold,fontSize=13.sp)
                            ThermoSchematic(kind)
                            Text(desc,color=muted,fontSize=11.sp,lineHeight=17.sp)
                        }
                    }
                }
                Text(if(fa)"سه نمودار این بخش بر اساس تصاویر مرجع ارسالی بازترسیم شده‌اند. منحنی‌ها و مقیاس محورها شماتیک‌اند و برای استخراج مقدار عددی استفاده نمی‌شوند." else "The three diagrams in this section are educational redrawings based on your supplied references. Curves and axes are schematic and must not be used to read numerical values.",color=muted,fontSize=10.sp)
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
    Canvas(Modifier.fillMaxWidth().height(190.dp)) {
        val l=43f; val r=size.width-14f; val top=12f; val b=size.height-30f
        val w=r-l; val h=b-top
        val axis=Color(0xFF8FA8BD); val domeColor=Color(0xFF35B6FF)
        drawLine(axis,Offset(l,b),Offset(r,b),2f)
        drawLine(axis,Offset(l,b),Offset(l,top),2f)
        if(kind=="P-T") {
            val triple=Offset(l+w*.43f,b-h*.34f)
            val critical=Offset(l+w*.88f,top+h*.30f)
            val sublimation=Path().apply { moveTo(l,b); cubicTo(l+w*.12f,b-h*.04f,l+w*.29f,b-h*.18f,triple.x,triple.y) }
            val vaporization=Path().apply { moveTo(triple.x,triple.y); cubicTo(l+w*.58f,b-h*.55f,l+w*.78f,top+h*.48f,critical.x,critical.y) }
            val fusion=Path().apply { moveTo(triple.x,triple.y); cubicTo(l+w*.36f,b-h*.56f,l+w*.38f,top+h*.18f,l+w*.37f,top) }
            drawPath(sublimation,Color(0xFFB66A58),style=Stroke(3f))
            drawPath(vaporization,Color(0xFF35B6FF),style=Stroke(3f))
            drawPath(fusion,Color(0xFF45A85A),style=Stroke(3f))
            drawLine(axis,Offset(triple.x,triple.y),Offset(triple.x,b),1f)
            drawLine(axis,Offset(l,triple.y),Offset(triple.x,triple.y),1f)
            drawCircle(Color(0xFFE85D5D),5f,triple)
            drawCircle(Color(0xFFE85D5D),5f,critical)
        } else {
            val dome=Path().apply {
                moveTo(l+w*.22f,b)
                cubicTo(l+w*.24f,b-h*.48f,l+w*.36f,top+h*.05f,l+w*.51f,top+h*.05f)
                cubicTo(l+w*.67f,top+h*.05f,l+w*.78f,b-h*.48f,l+w*.80f,b)
                close()
            }
            drawPath(dome,Color(0xFF35B6FF).copy(alpha=.12f))
            val boundary=Path().apply {
                moveTo(l+w*.22f,b)
                cubicTo(l+w*.24f,b-h*.48f,l+w*.36f,top+h*.05f,l+w*.51f,top+h*.05f)
                cubicTo(l+w*.67f,top+h*.05f,l+w*.78f,b-h*.48f,l+w*.80f,b)
            }
            drawPath(boundary,domeColor,style=Stroke(3f))
            drawCircle(Color(0xFFE85D5D),5f,Offset(l+w*.51f,top+h*.05f))
            if(kind=="P-v") {
                for(f in listOf(.34f,.53f,.72f)) {
                    val y=b-h*f
                    drawLine(Color(0xFFB66A58),Offset(l+w*.28f,y),Offset(l+w*.74f,y),2f)
                }
                for(f in listOf(.23f,.48f,.72f)) {
                    val p=Path().apply { moveTo(l+w*.30f,b-h*f); cubicTo(l+w*.42f,b-h*(f+.08f),l+w*.60f,b-h*(f+.18f),l+w*.91f,b-h*(f+.34f)) }
                    drawPath(p,Color(0xFFB66A58),style=Stroke(2f))
                }
            } else if(kind=="T-v") {
                for(f in listOf(.28f,.48f,.68f)) {
                    val p=Path().apply { moveTo(l+w*.10f,b-h*f); cubicTo(l+w*.35f,b-h*(f+.05f),l+w*.64f,b-h*(f+.10f),l+w*.93f,b-h*(f+.22f)) }
                    drawPath(p,Color(0xFFB66A58),style=Stroke(2f))
                }
            } else {
                val process=Path().apply { moveTo(l+w*.12f,b-h*.18f); cubicTo(l+w*.35f,b-h*.32f,l+w*.58f,b-h*.45f,l+w*.91f,b-h*.72f) }
                drawPath(process,Color(0xFFFFB86B),style=Stroke(2.5f))
            }
        }
    }
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) {
        Text(if(kind=="P-v"||kind=="T-v")"P / T" else "P",color=Color(0xFF35B6FF),fontSize=10.sp)
        Text(if(kind=="P-T")"T" else if(kind=="T-s")"s" else if(kind=="P-h")"h" else "v",color=Color(0xFF35B6FF),fontSize=10.sp)
    }
}
