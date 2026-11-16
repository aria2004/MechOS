package com.mechnos.app

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable

private data class OnlineResource(val title: String, val subtitle: String, val url: String)

@Composable
fun OnlineHubPage(fa: Boolean, fg: Color, muted: Color, surface: Color) {
    val context = LocalContext.current
    val resources = listOf(
        OnlineResource("NIST Chemistry WebBook", if (fa) "داده‌های خواص ترموفیزیکی مواد و ترکیبات" else "Thermophysical properties of chemicals", "https://webbook.nist.gov/chemistry/"),
        OnlineResource("Engineering ToolBox", if (fa) "جداول مهندسی سیالات، انرژی و مواد" else "Engineering tables for fluids, energy and materials", "https://www.engineeringtoolbox.com/"),
        OnlineResource("MatWeb", if (fa) "پایگاه داده خواص مواد و آلیاژها" else "Material and alloy property database", "https://www.matweb.com/"),
        OnlineResource("OpenStax University Physics", if (fa) "مطالعه آنلاین سه جلد فیزیک دانشگاهی" else "Read all three university physics volumes online", "https://openstax.org/subjects/science"),
        OnlineResource("MIT OpenCourseWare", if (fa) "درس‌ها، ویدئوها و تمرین‌های دانشگاه MIT" else "MIT courses, lectures and problem sets", "https://ocw.mit.edu/"),
        OnlineResource("NIST Fundamental Constants", if (fa) "ثابت‌های بنیادی و استانداردهای اندازه‌گیری" else "Fundamental constants and measurement references", "https://physics.nist.gov/cuu/Constants/")
    )
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 20.dp)) {
        item {
            Text(if (fa) "مرکز منابع آنلاین" else "Online Engineering Hub", color = fg, fontSize = 23.sp, fontWeight = FontWeight.Bold)
            Text(if (fa) "برای استفاده از این بخش اتصال اینترنت لازم است؛ منابع در مرورگر باز می‌شوند."
                else "Internet connection required. Resources open in your browser.", color = muted, fontSize = 12.sp)
        }
        items(resources) { resource ->
            Surface(color = surface, shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().clickable { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(resource.url))) }) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Language, contentDescription = null, tint = Color(0xFF35B6FF), modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(resource.title, color = fg, fontWeight = FontWeight.Bold)
                        Text(resource.subtitle, color = muted, fontSize = 11.sp)
                    }
                    Icon(Icons.Default.OpenInNew, contentDescription = null, tint = muted)
                }
            }
        }
        item {
            Text(if (fa) "برای طراحی نهایی، داده‌ها، واحدها و شرایط مرجع منابع بیرونی را بررسی کن."
                else "Verify units and reference conditions before using external data in final engineering designs.",
                color = muted, fontSize = 11.sp)
        }
    }
}