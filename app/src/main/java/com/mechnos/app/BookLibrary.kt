package com.mechnos.app

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun BookLibraryPage(fa: Boolean, fg: Color, muted: Color, surface: Color) {
    val context = LocalContext.current
    var opening by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(if (fa) "کتابخانه مهندسی آفلاین" else "Offline Engineering Library",
            color = fg, fontSize = 23.sp, fontWeight = FontWeight.Bold)
        Text(if (fa) "منابع آموزشی حجیم و قابل مطالعه بدون اینترنت"
            else "Large learning resources, available without internet", color = muted, fontSize = 12.sp)
        Surface(color = surface, shape = RoundedCornerShape(18.dp)) {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color(0xFF35B6FF))
                Text("University Physics • Volume 1", color = fg, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(if (fa) "مکانیک، حرکت، نیرو، کار و انرژی، تکانه، دوران، گرانش، نوسان و موج"
                    else "Mechanics, motion, forces, work and energy, momentum, rotation, gravitation, oscillations and waves",
                    color = muted, fontSize = 12.sp)
                Text(if (fa) "کتاب کامل OpenStax؛ فایل PDF پس از نصب داخل برنامه قرار دارد."
                    else "Full OpenStax textbook; the PDF is bundled with the app.",
                    color = muted, fontSize = 11.sp)
                Button(
                    onClick = {
                        if (opening) return@Button
                        opening = true
                        try {
                            val destination = File(context.cacheDir, "UniversityPhysicsVolume1.pdf")
                            if (!destination.exists() || destination.length() < 1_000_000L) {
                                context.assets.open("UniversityPhysicsVolume1.pdf").use { input ->
                                    destination.outputStream().use { output -> input.copyTo(output) }
                                }
                            }
                            val uri = FileProvider.getUriForFile(
                                context, "${context.packageName}.fileprovider", destination
                            )
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(uri, "application/pdf")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(intent)
                        } catch (_: ActivityNotFoundException) {
                            Toast.makeText(context, if (fa) "برنامه نمایش PDF نصب نیست." else "No PDF viewer is installed.", Toast.LENGTH_LONG).show()
                        } catch (_: Exception) {
                            Toast.makeText(context, if (fa) "فایل کتاب در دسترس نیست؛ نصب برنامه را بررسی کن." else "Book file unavailable. Check the app installation.", Toast.LENGTH_LONG).show()
                        } finally {
                            opening = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !opening
                ) {
                    Text(if (opening) (if (fa) "در حال باز کردن..." else "Opening...")
                        else (if (fa) "مطالعه کتاب آفلاین" else "Read offline textbook"))
                }
                Text("Authors: William Moebs, Samuel J. Ling, Jeff Sanny • OpenStax • CC BY 4.0 • Access for free at openstax.org",
                    color = muted, fontSize = 10.sp)
            }
        }
        Text(if (fa) "منابع بعدی در نسخه‌های آینده: ترمودینامیک، مقاومت مصالح، دینامیک ماشین و طراحی اجزا."
            else "Planned next: thermodynamics, strength of materials, machine dynamics and machine design.",
            color = muted, fontSize = 12.sp)
    }
}
