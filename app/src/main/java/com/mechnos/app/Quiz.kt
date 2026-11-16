package com.mechnos.app

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuizPage(fa:Boolean, fg:Color, muted:Color, surface:Color) {
    val context = LocalContext.current
    var course by remember { mutableStateOf<Lesson?>(null) }
    var questions by remember { mutableStateOf(emptyList<LessonQuestion>()) }
    var index by remember { mutableIntStateOf(0) }
    var revealed by remember { mutableStateOf(false) }
    var mastered by remember { mutableIntStateOf(0) }
    var reviewed by remember { mutableIntStateOf(0) }
    var finished by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalLayoutDirection provides if(fa) LayoutDirection.Rtl else LayoutDirection.Ltr) {
        Column(Modifier.fillMaxSize(), verticalArrangement=Arrangement.spacedBy(12.dp)) {
            Text(if(fa) "تمرین و آزمون" else "Practice & Quiz", color=fg, fontSize=23.sp, fontWeight=FontWeight.Bold)
            Text(if(fa) "یادآوری فعال؛ اول جواب بده، بعد پاسخ را ببین و میزان تسلطت را ثبت کن." else "Active recall: try first, reveal the solution, then rate your confidence.", color=muted, fontSize=12.sp)

            if(course == null) {
                Text(if(fa) "یک درس انتخاب کن" else "Choose a course", color=fg, fontWeight=FontWeight.Bold)
                LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp), contentPadding=PaddingValues(bottom=16.dp)) {
                    items(lessons.size) { i ->
                        val lesson=lessons[i]
                        Surface(color=surface, shape=RoundedCornerShape(14.dp), modifier=Modifier.fillMaxWidth().clickable {
                            course=lesson
                            questions=lesson.questions.shuffled()
                            index=0; revealed=false; mastered=0; reviewed=0; finished=false
                        }) {
                            Row(Modifier.padding(15.dp), verticalAlignment=Alignment.CenterVertically) {
                                Icon(Icons.Default.School, null, tint=Color(0xFF35AEEF), modifier=Modifier.size(25.dp))
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(if(fa) lesson.fa else lesson.en, color=fg, fontWeight=FontWeight.Bold)
                                    Text(if(fa) "${lesson.questions.size} سؤال تمرینی" else "${lesson.questions.size} practice questions", color=muted, fontSize=11.sp)
                                }
                                Text(if(fa) "شروع ←" else "Start →", color=Color(0xFF35AEEF), fontSize=12.sp)
                            }
                        }
                    }
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
                        Button(onClick={course=null; questions= emptyList()}, modifier=Modifier.fillMaxWidth()) { Text(if(fa) "انتخاب درس دیگر" else "Choose another course") }
                        OutlinedButton(onClick={
                            questions=course!!.questions.shuffled(); index=0; mastered=0; reviewed=0; revealed=false; finished=false
                        }, modifier=Modifier.fillMaxWidth()) {
                            Icon(Icons.Default.Refresh,null); Spacer(Modifier.width(6.dp)); Text(if(fa) "تکرار آزمون" else "Try again")
                        }
                    }
                }
            } else {
                val current=questions[index]
                Row(verticalAlignment=Alignment.CenterVertically) {
                    IconButton(onClick={course=null; finished=false}) { Icon(Icons.Default.ArrowBack,null,tint=fg) }
                    Column(Modifier.weight(1f)) {
                        Text(if(fa) course!!.fa else course!!.en, color=fg, fontWeight=FontWeight.Bold)
                        Text(if(fa) "سؤال ${index+1} از ${questions.size}" else "Question ${index+1} of ${questions.size}", color=muted, fontSize=11.sp)
                    }
                }
                LinearProgressIndicator(progress={(index+1).toFloat()/questions.size.coerceAtLeast(1)}, modifier=Modifier.fillMaxWidth(), color=Color(0xFF35AEEF))
                Surface(color=surface, shape=RoundedCornerShape(18.dp)) {
                    Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement=Arrangement.spacedBy(14.dp)) {
                        Text(if(fa) "سؤال" else "QUESTION", color=Color(0xFF35AEEF), fontSize=10.sp, fontWeight=FontWeight.Bold)
                        Text(if(fa) current.qFa else current.qEn, color=fg, fontSize=16.sp, fontWeight=FontWeight.SemiBold)
                        if(revealed) {
                            HorizontalDivider(color=muted.copy(alpha=.25f))
                            Text(if(fa) "پاسخ تشریحی" else "SOLUTION", color=Color(0xFF36C98F), fontSize=10.sp, fontWeight=FontWeight.Bold)
                            Text(if(fa) current.aFa else current.aEn, color=fg, fontSize=14.sp, lineHeight=22.sp)
                        } else {
                            Text(if(fa) "قبل از دیدن پاسخ، خودت مسئله را حل کن یا جواب را با صدای بلند توضیح بده." else "Before revealing the answer, solve it or explain your reasoning aloud.", color=muted, fontSize=12.sp)
                        }
                    }
                }
                if(!revealed) {
                    Button(onClick={revealed=true}, modifier=Modifier.fillMaxWidth()) { Text(if(fa) "نمایش پاسخ" else "Reveal solution") }
                } else {
                    Text(if(fa) "چقدر به پاسخ مسلط بودی؟" else "How well did you know it?", color=fg, fontWeight=FontWeight.Bold)
                    Button(onClick={
                        mastered++
                        if(index+1>=questions.size) finished=true else { index++; revealed=false }
                    }, modifier=Modifier.fillMaxWidth(), colors=ButtonDefaults.buttonColors(containerColor=Color(0xFF167A59))) {
                        Text(if(fa) "بلدم ✓" else "I knew it ✓")
                    }
                    OutlinedButton(onClick={
                        reviewed++
                        if(index+1>=questions.size) finished=true else { index++; revealed=false }
                    }, modifier=Modifier.fillMaxWidth()) {
                        Text(if(fa) "نیاز به مرور دارم" else "Needs review")
                    }
                }
            }
        }
    }
}
