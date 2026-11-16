package com.mechnos.app

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizPage(fa:Boolean, fg:Color, muted:Color, surface:Color) {
    var course by remember { mutableStateOf<Lesson?>(null) }
    var level by remember { mutableStateOf("ALL") }
    var questions by remember { mutableStateOf(emptyList<PracticeQuestion>()) }
    var started by remember { mutableStateOf(false) }
    var index by remember { mutableIntStateOf(0) }
    var revealed by remember { mutableStateOf(false) }
    var mastered by remember { mutableIntStateOf(0) }
    var reviewed by remember { mutableIntStateOf(0) }
    var finished by remember { mutableStateOf(false) }
    fun start() {
        val all=practiceQuestionBank(course!!)
        questions=(if(level=="ALL") all else all.filter { it.difficulty.name==level }).shuffled()
        index=0; revealed=false; mastered=0; reviewed=0; finished=false; started=true
    }
    CompositionLocalProvider(LocalLayoutDirection provides if(fa) LayoutDirection.Rtl else LayoutDirection.Ltr) {
        Column(Modifier.fillMaxSize(), verticalArrangement=Arrangement.spacedBy(12.dp)) {
            Text(if(fa) "تمرین و آزمون" else "Practice & Quiz", color=fg, fontSize=23.sp, fontWeight=FontWeight.Bold)
            Text(if(fa) "۱۰ درس و مجموعاً ۱۰۰۰ سؤال؛ هر درس ۱۰۰ سؤال سطح‌بندی‌شده دارد." else "1,000 questions across 10 courses — 100 per course, with three difficulty levels.", color=muted, fontSize=12.sp)

            if(course==null) {
                Text(if(fa) "یک درس انتخاب کن" else "Choose a course", color=fg, fontWeight=FontWeight.Bold)
                LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp), contentPadding=PaddingValues(bottom=16.dp)) {
                    items(lessons.size) { i ->
                        val lesson=lessons[i]
                        Surface(color=surface, shape=RoundedCornerShape(14.dp), modifier=Modifier.fillMaxWidth().clickable {
                            course=lesson; level="ALL"; started=false; finished=false
                        }) {
                            Row(Modifier.padding(15.dp), verticalAlignment=Alignment.CenterVertically) {
                                Icon(Icons.Default.School, null, tint=Color(0xFF35AEEF), modifier=Modifier.size(25.dp))
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(if(fa) lesson.fa else lesson.en, color=fg, fontWeight=FontWeight.Bold)
                                    Text(if(fa) "۱۰۰ سؤال • آسان، متوسط و دشوار" else "100 questions • Easy, medium & hard", color=muted, fontSize=11.sp)
                                }
                                Text(if(fa) "انتخاب ←" else "Select →", color=Color(0xFF35AEEF), fontSize=12.sp)
                            }
                        }
                    }
                }
            } else if(!started) {
                Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment=Alignment.CenterVertically) {
                        IconButton(onClick={course=null}) { Icon(Icons.Default.ArrowBack,null,tint=fg) }
                        Text(if(fa) course!!.fa else course!!.en,color=fg,fontWeight=FontWeight.Bold,fontSize=18.sp)
                    }
                    Text(if(fa) "سطح دشواری را انتخاب کن" else "Choose difficulty", color=fg, fontWeight=FontWeight.Bold)
                    val levels=listOf("ALL","EASY","MEDIUM","HARD")
                    levels.forEach { v ->
                        val selected=level==v
                        Surface(color=if(selected) Color(0xFF123E60) else surface, shape=RoundedCornerShape(13.dp),
                            modifier=Modifier.fillMaxWidth().clickable { level=v }) {
                            Row(Modifier.padding(15.dp),verticalAlignment=Alignment.CenterVertically) {
                                RadioButton(selected=selected,onClick={level=v})
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text(when(v){"ALL"->if(fa)"همه سؤال‌ها" else "All questions";"EASY"->if(fa)"آسان" else "Easy";"MEDIUM"->if(fa)"متوسط" else "Medium";else->if(fa)"دشوار" else "Hard"},color=fg,fontWeight=FontWeight.SemiBold)
                                    Text(when(v){"ALL"->if(fa)"۱۰۰ سؤال" else "100 questions";"EASY"->if(fa)"۳۰ سؤال" else "30 questions";"MEDIUM"->if(fa)"۴۰ سؤال" else "40 questions";else->if(fa)"۳۰ سؤال" else "30 questions"},color=muted,fontSize=11.sp)
                                }
                            }
                        }
                    }
                    Button(onClick={start()},modifier=Modifier.fillMaxWidth().heightIn(min=52.dp)) { Text(if(fa)"تأیید و شروع تمرین" else "Confirm & start") }
                }
            } else if(finished) {
                Surface(color=surface, shape=RoundedCornerShape(18.dp)) {
                    Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Default.CheckCircle, null, tint=Color(0xFF36C98F), modifier=Modifier.size(48.dp))
                        Text(if(fa) "تمرین تمام شد" else "Practice completed", color=fg, fontSize=20.sp, fontWeight=FontWeight.Bold)
                        Text(if(fa) course!!.fa else course!!.en, color=muted)
                        Text("$mastered / ${questions.size}", color=Color(0xFF35AEEF), fontSize=30.sp, fontWeight=FontWeight.Black)
                        Text(if(fa) "مسلط" else "Mastered", color=fg, fontWeight=FontWeight.Bold)
                        Text(if(fa) "$reviewed سؤال برای مرور دوباره علامت خورد." else "$reviewed questions marked for review.", color=muted, fontSize=12.sp)
                        Button(onClick={started=false; finished=false}, modifier=Modifier.fillMaxWidth()) { Text(if(fa) "انتخاب سطح دیگر" else "Choose another level") }
                        OutlinedButton(onClick={start()}, modifier=Modifier.fillMaxWidth()) {
                            Icon(Icons.Default.Refresh,null); Spacer(Modifier.width(6.dp)); Text(if(fa) "تکرار آزمون" else "Try again")
                        }
                    }
                }
            } else {
                val current=questions[index]
                Row(verticalAlignment=Alignment.CenterVertically) {
                    IconButton(onClick={started=false;finished=false}) { Icon(Icons.Default.ArrowBack,null,tint=fg) }
                    Column(Modifier.weight(1f)) {
                        Text(if(fa) course!!.fa else course!!.en, color=fg, fontWeight=FontWeight.Bold)
                        Text(if(fa) "سؤال ${index+1} از ${questions.size}" else "Question ${index+1} of ${questions.size}", color=muted, fontSize=11.sp)
                    }
                    Text(when(current.difficulty){Difficulty.EASY->if(fa)"آسان" else "Easy";Difficulty.MEDIUM->if(fa)"متوسط" else "Medium";Difficulty.HARD->if(fa)"دشوار" else "Hard"},color=Color(0xFF35AEEF),fontSize=11.sp,fontWeight=FontWeight.Bold)
                }
                LinearProgressIndicator(progress={(index+1).toFloat()/questions.size.coerceAtLeast(1)}, modifier=Modifier.fillMaxWidth(), color=Color(0xFF35AEEF))
                if(index % 10 == 0) {
                    Surface(color=surface,shape=RoundedCornerShape(15.dp)) {
                        Column(Modifier.fillMaxWidth().padding(10.dp),verticalArrangement=Arrangement.spacedBy(5.dp)) {
                            Text(if(fa)"سؤال تصویری • شکل راهنما" else "Visual question • reference sketch",color=Color(0xFF35AEEF),fontSize=11.sp,fontWeight=FontWeight.Bold)
                            QuestionSketch(course!!.id)
                        }
                    }
                }
                Surface(color=surface, shape=RoundedCornerShape(18.dp)) {
                    Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement=Arrangement.spacedBy(14.dp)) {
                        Text(if(fa) "سؤال ${index+1}" else "QUESTION ${index+1}", color=Color(0xFF35AEEF), fontSize=10.sp, fontWeight=FontWeight.Bold)
                        Text(if(fa) current.qFa else current.qEn, color=fg, fontSize=16.sp, fontWeight=FontWeight.SemiBold)
                        if(revealed) {
                            HorizontalDivider(color=muted.copy(alpha=.25f))
                            Text(if(fa) "پاسخ تشریحی" else "WORKED SOLUTION", color=Color(0xFF36C98F), fontSize=10.sp, fontWeight=FontWeight.Bold)
                            Text(if(fa) current.aFa else current.aEn, color=fg, fontSize=14.sp, lineHeight=22.sp)
                        } else {
                            Text(if(fa) "قبل از دیدن پاسخ، خودت مسئله را حل کن." else "Try solving it before revealing the solution.", color=muted, fontSize=12.sp)
                        }
                    }
                }
                if(!revealed) {
                    Button(onClick={revealed=true}, modifier=Modifier.fillMaxWidth()) { Text(if(fa) "نمایش پاسخ مرحله‌ای" else "Reveal worked solution") }
                } else {
                    Text(if(fa) "چقدر به پاسخ مسلط بودی؟" else "How well did you know it?", color=fg, fontWeight=FontWeight.Bold)
                    Button(onClick={mastered++; if(index+1>=questions.size) finished=true else {index++;revealed=false}}, modifier=Modifier.fillMaxWidth(), colors=ButtonDefaults.buttonColors(containerColor=Color(0xFF167A59))) { Text(if(fa) "بلدم ✓" else "I knew it ✓") }
                    OutlinedButton(onClick={reviewed++; if(index+1>=questions.size) finished=true else {index++;revealed=false}}, modifier=Modifier.fillMaxWidth()) { Text(if(fa) "نیاز به مرور دارم" else "Needs review") }
                }
            }
        }
    }
}

@Composable
private fun QuestionSketch(courseId:String) {
    Canvas(Modifier.fillMaxWidth().height(112.dp)) {
        val w=size.width; val h=size.height
        val ink=Color(0xFF58BDF5); val soft=Color(0xFF8FA8BD)
        fun line(x1:Float,y1:Float,x2:Float,y2:Float,color:Color=ink,sw:Float=3f) { drawLine(color,Offset(x1,y1),Offset(x2,y2),sw) }
        when(courseId) {
            "statics" -> {
                line(w*.15f,h*.62f,w*.85f,h*.62f)
                val p=Path().apply{moveTo(w*.22f,h*.62f);lineTo(w*.17f,h*.82f);lineTo(w*.27f,h*.82f);close()}; drawPath(p,soft)
                val p2=Path().apply{moveTo(w*.78f,h*.62f);lineTo(w*.73f,h*.82f);lineTo(w*.83f,h*.82f);close()}; drawPath(p2,soft)
                line(w*.52f,h*.12f,w*.52f,h*.55f);line(w*.52f,h*.55f,w*.49f,h*.48f);line(w*.52f,h*.55f,w*.55f,h*.48f)
            }
            "strength" -> {
                drawRect(ink,Offset(w*.27f,h*.38f),androidx.compose.ui.geometry.Size(w*.46f,h*.25f),style=Stroke(3f))
                line(w*.27f,h*.5f,w*.12f,h*.5f);line(w*.12f,h*.5f,w*.17f,h*.45f);line(w*.12f,h*.5f,w*.17f,h*.55f)
                line(w*.73f,h*.5f,w*.88f,h*.5f);line(w*.88f,h*.5f,w*.83f,h*.45f);line(w*.88f,h*.5f,w*.83f,h*.55f)
            }
            "thermo" -> {
                drawRect(ink,Offset(w*.3f,h*.25f),androidx.compose.ui.geometry.Size(w*.4f,h*.62f),style=Stroke(3f))
                line(w*.3f,h*.34f,w*.7f,h*.34f,soft,4f)
                line(w*.5f,h*.05f,w*.5f,h*.28f);line(w*.5f,h*.28f,w*.46f,h*.22f);line(w*.5f,h*.28f,w*.54f,h*.22f)
            }
            "fluids" -> {
                line(w*.12f,h*.35f,w*.88f,h*.35f);line(w*.12f,h*.7f,w*.88f,h*.7f)
                for(i in 0..2){val x=w*(.28f+i*.22f);line(x,h*.52f,x+w*.1f,h*.52f);line(x+w*.1f,h*.52f,x+w*.06f,h*.48f);line(x+w*.1f,h*.52f,x+w*.06f,h*.56f)}
            }
            "heat" -> {
                drawRect(soft,Offset(w*.35f,h*.2f),androidx.compose.ui.geometry.Size(w*.3f,h*.62f),style=Stroke(3f))
                for(i in 0..2){val y=h*(.3f+i*.2f);line(w*.12f,y,w*.32f,y);line(w*.32f,y,w*.27f,y-.04f*h);line(w*.32f,y,w*.27f,y+.04f*h)}
            }
            "dynamics" -> {
                drawRect(ink,Offset(w*.38f,h*.4f),androidx.compose.ui.geometry.Size(w*.22f,h*.25f),style=Stroke(3f))
                line(w*.6f,h*.52f,w*.86f,h*.52f);line(w*.86f,h*.52f,w*.8f,h*.46f);line(w*.86f,h*.52f,w*.8f,h*.58f)
                line(w*.12f,h*.8f,w*.9f,h*.8f,soft,2f)
            }
            "machine" -> {
                drawCircle(ink,w*.19f,Offset(w*.4f,h*.52f),style=Stroke(3f));drawCircle(soft,w*.15f,Offset(w*.68f,h*.52f),style=Stroke(3f))
                drawCircle(ink,w*.035f,Offset(w*.4f,h*.52f));drawCircle(soft,w*.03f,Offset(w*.68f,h*.52f))
            }
            "design" -> {
                line(w*.18f,h*.52f,w*.82f,h*.52f,soft,10f);line(w*.28f,h*.25f,w*.28f,h*.78f);line(w*.72f,h*.25f,w*.72f,h*.78f)
                drawCircle(ink,w*.12f,Offset(w*.5f,h*.52f),style=Stroke(3f))
            }
            "vibrations" -> {
                line(w*.48f,h*.12f,w*.48f,h*.23f,soft)
                val spring=Path().apply{moveTo(w*.48f,h*.23f);for(i in 0..8)lineTo(w*(if(i%2==0).43f else .53f),h*(.25f+i*.055f));lineTo(w*.48f,h*.7f)}
                drawPath(spring,ink,style=Stroke(3f));drawRect(soft,Offset(w*.36f,h*.7f),androidx.compose.ui.geometry.Size(w*.24f,h*.18f))
            }
            else -> {
                val axisY=h*.78f;line(w*.12f,axisY,w*.9f,axisY,soft,2f);line(w*.18f,h*.9f,w*.18f,h*.12f,soft,2f)
                val curve=Path().apply{moveTo(w*.2f,h*.72f);cubicTo(w*.35f,h*.1f,w*.62f,h*.1f,w*.82f,h*.55f)}
                drawPath(curve,ink,style=Stroke(3f))
            }
        }
    }
}
