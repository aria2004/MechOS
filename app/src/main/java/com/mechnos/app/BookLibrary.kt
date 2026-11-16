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

private data class OfflineBook(val title: String, val subtitle: String, val file: String, val attribution: String)

@Composable
fun BookLibraryPage(fa: Boolean, fg: Color, muted: Color, surface: Color) {
    val context = LocalContext.current
    var opening by remember { mutableStateOf(false) }
    val books = listOf(
        OfflineBook("University Physics • Volume 1",
            if (fa) "مکانیک، حرکت، نیرو، انرژی، تکانه، دوران، گرانش و موج" else "Mechanics, motion, forces, energy, momentum, rotation, gravitation and waves",
            "UniversityPhysicsVolume1.pdf", "OpenStax • CC BY-NC-SA 4.0 • openstax.org"),
        OfflineBook("University Physics • Volume 2",
            if (fa) "دما و گرما، ترمودینامیک، الکتریسیته و مغناطیس" else "Temperature, heat, thermodynamics, electricity and magnetism",
            "UniversityPhysicsVolume2.pdf", "OpenStax • CC BY 4.0 • openstax.org"),
        OfflineBook("University Physics • Volume 3",
            if (fa) "نورشناسی، تداخل، پراش، نسبیت و فیزیک کوانتومی" else "Optics, interference, diffraction, relativity and quantum physics",
            "UniversityPhysicsVolume3.pdf", "OpenStax • CC BY 4.0 • openstax.org")
    )
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(if (fa) "کتابخانه مهندسی آفلاین" else "Offline Engineering Library",
            color = fg, fontSize = 23.sp, fontWeight = FontWeight.Bold)
        Text(if (fa) "سه جلد مرجع دانشگاهی؛ پس از نصب بدون اینترنت قابل مطالعه‌اند."
            else "Three university reference volumes, available offline after installation.", color = muted, fontSize = 12.sp)
        books.forEachIndexed { index, book ->
            Surface(color = surface, shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color(0xFF35B6FF))
                    Text(book.title, color = fg, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text(book.subtitle, color = muted, fontSize = 12.sp)
                    Button(onClick = {
                        if (opening) return@Button
                        opening = true
                        try {
                            val destination = File(context.cacheDir, book.file)
                            if (!destination.exists() || destination.length() < 1_000_000L) {
                                context.assets.open(book.file).use { input ->
                                    destination.outputStream().use { output -> input.copyTo(output) }
                                }
                            }
                            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", destination)
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                setDataAndType(uri, "application/pdf")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(intent)
                        } catch (_: ActivityNotFoundException) {
                            Toast.makeText(context, if (fa) "برنامه نمایش PDF نصب نیست." else "No PDF viewer is installed.", Toast.LENGTH_LONG).show()
                        } catch (_: Exception) {
                            Toast.makeText(context, if (fa) "فایل کتاب در دسترس نیست؛ نصب برنامه را بررسی کن." else "Book unavailable. Check the app installation.", Toast.LENGTH_LONG).show()
                        } finally { opening = false }
                    }, modifier = Modifier.fillMaxWidth(), enabled = !opening) {
                        Text(if (opening) (if (fa) "در حال باز کردن..." else "Opening...")
                            else (if (fa) "مطالعه جلد ${index + 1}" else "Read volume ${index + 1}"))
                    }
                    Text(book.attribution, color = muted, fontSize = 10.sp)
                }
            }
        }
    }
}