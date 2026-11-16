package com.mechnos.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.function.Function
import java.util.Locale
import kotlin.math.*

private data class CalcRecord(val expression: String, val result: String)

@Composable
fun ScientificCalculatorPage(fa: Boolean, fg: Color, muted: Color, surface: Color) {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }
    var error by remember { mutableStateOf(false) }
    var degrees by remember { mutableStateOf(true) }
    var advanced by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }
    var memory by remember { mutableDoubleStateOf(0.0) }
    var answer by remember { mutableDoubleStateOf(0.0) }
    val history = remember { mutableStateListOf<CalcRecord>() }

    fun insert(value: String) { expression += value; error = false }
    fun clear() { expression = ""; result = "0"; error = false }
    fun backspace() { if (expression.isNotEmpty()) expression = expression.dropLast(1) }

    fun calculate() {
        if (expression.isBlank()) return
        try {
            val raw = expression.replace("×", "*").replace("÷", "/").replace("−", "-")
                .replace("π", "pi").replace("Ans", "ans").replace("√(", "sqrt(")
            val angle = degrees
            val functions = arrayOf(
                object : Function("sin", 1) { override fun apply(vararg a: Double) = if (angle) sin(Math.toRadians(a[0])) else sin(a[0]) },
                object : Function("cos", 1) { override fun apply(vararg a: Double) = if (angle) cos(Math.toRadians(a[0])) else cos(a[0]) },
                object : Function("tan", 1) { override fun apply(vararg a: Double) = if (angle) tan(Math.toRadians(a[0])) else tan(a[0]) },
                object : Function("asin", 1) { override fun apply(vararg a: Double) = if (angle) Math.toDegrees(asin(a[0])) else asin(a[0]) },
                object : Function("acos", 1) { override fun apply(vararg a: Double) = if (angle) Math.toDegrees(acos(a[0])) else acos(a[0]) },
                object : Function("atan", 1) { override fun apply(vararg a: Double) = if (angle) Math.toDegrees(atan(a[0])) else atan(a[0]) },
                object : Function("sinh", 1) { override fun apply(vararg a: Double) = sinh(a[0]) },
                object : Function("cosh", 1) { override fun apply(vararg a: Double) = cosh(a[0]) },
                object : Function("tanh", 1) { override fun apply(vararg a: Double) = tanh(a[0]) },
                object : Function("ln", 1) { override fun apply(vararg a: Double) = ln(a[0]) },
                object : Function("log", 1) { override fun apply(vararg a: Double) = log10(a[0]) },
                object : Function("sqrt", 1) { override fun apply(vararg a: Double) = sqrt(a[0]) },
                object : Function("cbrt", 1) { override fun apply(vararg a: Double) = cbrt(a[0]) },
                object : Function("abs", 1) { override fun apply(vararg a: Double) = abs(a[0]) },
                object : Function("floor", 1) { override fun apply(vararg a: Double) = floor(a[0]) },
                object : Function("ceil", 1) { override fun apply(vararg a: Double) = ceil(a[0]) },
                object : Function("exp", 1) { override fun apply(vararg a: Double) = exp(a[0]) },
                object : Function("fact", 1) { override fun apply(vararg a: Double): Double {
                    val n = a[0].toInt()
                    require(a[0] >= 0 && a[0] == n.toDouble() && n <= 170)
                    var v = 1.0
                    for (i in 2..n) v *= i
                    return v
                } }
            )
            val value = ExpressionBuilder(raw).variables("ans").functions(*functions)
                .build().setVariable("ans", answer).evaluate()
            require(value.isFinite())
            answer = value
            result = formatNumber(value)
            error = false
            history.add(0, CalcRecord(expression, result))
            if (history.size > 30) history.removeAt(history.lastIndex)
        } catch (_: Exception) {
            result = if (fa) "عبارت نامعتبر" else "Invalid expression"
            error = true
        }
    }

    val keys = if (advanced) listOf(
        listOf("sin(", "cos(", "tan(", "asin(", "acos("),
        listOf("sinh(", "cosh(", "tanh(", "atan(", "π"),
        listOf("ln(", "log(", "abs(", "√(", "∛("),
        listOf("x²", "x³", "^", "fact(", "e"),
        listOf("(", ")", "floor(", "ceil(", "÷"),
        listOf("7", "8", "9", "×", "⌫"),
        listOf("4", "5", "6", "−", "AC"),
        listOf("1", "2", "3", "+", "Ans"),
        listOf("±", "0", ".", "%", "=")
    ) else listOf(
        listOf("sin(", "cos(", "tan(", "π", "÷"),
        listOf("√(", "ln(", "log(", "^", "×"),
        listOf("7", "8", "9", "−", "⌫"),
        listOf("4", "5", "6", "+", "AC"),
        listOf("1", "2", "3", "(", ")"),
        listOf("±", "0", ".", "Ans", "=")
    )

    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(if (fa) "ماشین‌حساب علمی" else "Scientific Calculator", color = fg, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(if (fa) "از محاسبات پایه تا توابع پیشرفته" else "From basic math to advanced functions", color = muted, fontSize = 11.sp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                FilterChip(selected = degrees, onClick = { degrees = true }, label = { Text("DEG") })
                FilterChip(selected = !degrees, onClick = { degrees = false }, label = { Text("RAD") })
            }
        }
        Surface(color = surface, shape = RoundedCornerShape(20.dp)) {
            Column(Modifier.fillMaxWidth().padding(14.dp), horizontalAlignment = Alignment.End) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(if (fa) "عبارت" else "EXPRESSION", color = muted, fontSize = 10.sp)
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { showHistory = !showHistory }) {
                        Icon(Icons.Default.History, null, tint = muted, modifier = Modifier.size(17.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(if (fa) "تاریخچه" else "History", color = muted, fontSize = 11.sp)
                    }
                }
                Text(expression.ifBlank { "0" }, color = fg, fontSize = 23.sp, textAlign = TextAlign.End, maxLines = 2, modifier = Modifier.fillMaxWidth().padding(top = 10.dp))
                Text(result, color = if (error) Color(0xFFFF6B6B) else Color(0xFF35B6FF), fontSize = 31.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End, maxLines = 1, modifier = Modifier.fillMaxWidth().padding(top = 4.dp))
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { advanced = !advanced }) { Text(if (advanced) (if (fa) "حالت ساده" else "Basic") else (if (fa) "توابع بیشتر ▾" else "More functions ▾")) }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Memory, null, tint = muted, modifier = Modifier.size(16.dp))
                Text("M: " + formatNumber(memory), color = muted, fontSize = 10.sp)
                TextButton(onClick = { memory = 0.0 }) { Text("MC", fontSize = 10.sp) }
                TextButton(onClick = { memory = answer }) { Text("M+", fontSize = 10.sp) }
                TextButton(onClick = { insert(formatNumber(memory)) }) { Text("MR", fontSize = 10.sp) }
            }
        }
        if (showHistory) {
            Surface(color = surface, shape = RoundedCornerShape(15.dp)) {
                Column(Modifier.padding(9.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(if (fa) "محاسبات اخیر" else "Recent calculations", color = fg, fontWeight = FontWeight.Bold)
                        TextButton(onClick = { history.clear() }) { Text(if (fa) "پاک‌کردن" else "Clear") }
                    }
                    if (history.isEmpty()) Text(if (fa) "هنوز محاسبه‌ای ثبت نشده" else "No calculations yet", color = muted)
                    LazyColumn(Modifier.heightIn(max = 115.dp)) {
                        items(history) { item ->
                            Column(Modifier.fillMaxWidth().clickable { expression = item.expression; result = item.result; showHistory = false }.padding(vertical = 5.dp)) {
                                Text(item.expression, color = muted, fontSize = 11.sp)
                                Text(item.result, color = fg, fontWeight = FontWeight.SemiBold)
                            }
                            HorizontalDivider(color = muted.copy(alpha = .15f))
                        }
                    }
                }
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            keys.forEach { row ->
                Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    row.forEach { key ->
                        val accent = key in listOf("=", "AC", "⌫", "÷", "×", "−", "+")
                        val label = when (key) { "√(" -> "√"; "∛(" -> "∛"; "fact(" -> "n!"; else -> key }
                        Surface(
                            modifier = Modifier.weight(1f).fillMaxHeight().clickable {
                                when (key) {
                                    "AC" -> clear()
                                    "⌫" -> backspace()
                                    "=" -> calculate()
                                    "x²" -> insert("^2")
                                    "x³" -> insert("^3")
                                    "π", "e", "Ans" -> insert(key)
                                    "∛(" -> insert("cbrt(")
                                    "±" -> if (expression.isNotEmpty()) insert("×(-1)") else insert("-")
                                    else -> insert(key)
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            color = when {
                                key == "=" -> Color(0xFF087FC2)
                                key == "AC" || key == "⌫" -> Color(0xFF9B3D4A)
                                accent -> Color(0xFF1B4262)
                                else -> surface
                            }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(label, color = Color.White, fontSize = if (key.length > 5) 10.sp else 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                            }
                        }
                    }
                }
            }
        }
        Text(if (fa) "توابع مثلثاتی با حالت DEG یا RAD محاسبه می‌شوند." else "Trigonometry follows the selected DEG/RAD mode.", color = muted, fontSize = 10.sp, modifier = Modifier.padding(bottom = 2.dp))
    }
}

private fun formatNumber(value: Double): String {
    if (value == 0.0) return "0"
    if (abs(value) >= 1e12 || abs(value) < 1e-8) return String.format(Locale.US, "%.6e", value)
    return String.format(Locale.US, "%.10f", value).trimEnd('0').trimEnd('.')
}
