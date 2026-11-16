package com.mechnos.app

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class LessonFormula(val titleFa:String,val titleEn:String,val equation:String,val noteFa:String,val noteEn:String)
private data class LessonQuestion(val qFa:String,val qEn:String,val aFa:String,val aEn:String)
private data class Lesson(val id:String,val fa:String,val en:String,val descFa:String,val descEn:String,val formulas:List<LessonFormula>,val concepts:List<Pair<String,String>>,val questions:List<LessonQuestion>)

private fun q(f:String,e:String,af:String,ae:String)=LessonQuestion(f,e,af,ae)
private fun formula(t:String,e:String,n:String,ne:String)=LessonFormula(t,when(t) { "قانون تعادل نیروها"->"Force equilibrium"; "تعادل گشتاورها"->"Moment equilibrium"; "گشتاور نیرو"->"Moment of a force"; "اصطکاک حدی"->"Limiting friction"; "تنش نرمال"->"Normal stress"; "کرنش محوری"->"Axial strain"; "قانون هوک"->"Hooke’s law"; "تنش برشی متوسط"->"Average shear stress"; "خمش تیر"->"Beam bending"; "قانون اول سیستم بسته"->"First law for a closed system"; "گاز ایده‌آل"->"Ideal gas"; "کار مرزی"->"Boundary work"; "آنتالپی"->"Enthalpy"; "کیفیت بخار"->"Vapor quality"; "سرعت متوسط"->"Average velocity"; "شتاب"->"Acceleration"; "حرکت با شتاب ثابت"->"Constant-acceleration motion"; "قانون دوم نیوتن"->"Newton’s second law"; "انرژی جنبشی"->"Kinetic energy"; "فشار"->"Pressure"; "فشار هیدرواستاتیک"->"Hydrostatic pressure"; "معادله پیوستگی"->"Continuity equation"; "برنولی"->"Bernoulli equation"; "نیروی شناوری"->"Buoyant force"; "قانون فوریه"->"Fourier’s law"; "جابه‌جایی نیوتن"->"Newton’s law of cooling"; "قانون استفان-بولتزمن"->"Stefan–Boltzmann law"; "مقاومت رسانش دیواره"->"Wall conduction resistance"; "نسبت سرعت زاویه‌ای"->"Angular speed ratio"; "سرعت نقطه روی جسم صلب"->"Rigid-body point velocity"; "شتاب مماسی"->"Tangential acceleration"; "شتاب نرمال"->"Normal acceleration"; "تنش پیچشی محور"->"Shaft torsional stress"; "توان دورانی"->"Rotational power"; "تنش برشی پیچ"->"Bolt shear stress"; "ضریب اطمینان"->"Factor of safety"; "فرکانس طبیعی جرم-فنر"->"Mass-spring natural frequency"; "فرکانس"->"Frequency"; "نسبت میرایی"->"Damping ratio"; "میرایی بحرانی"->"Critical damping"; "مشتق توان"->"Power rule"; "انتگرال توان"->"Power integral"; "تبدیل لاپلاس"->"Laplace transform"; else->t },e,n,ne)


private fun extraFormula(t:String,en:String,eq:String,faNote:String,enNote:String)=LessonFormula(t,en,eq,faNote,enNote)
private fun extraFormulas(id:String):List<LessonFormula> = when(id) {
"statics" -> listOf(
extraFormula("تجزیه نیرو","Force components","Fx = F cosθ ; Fy = F sinθ","زاویه از محور x مثبت اندازه‌گیری می‌شود.","Angle is measured from the positive x-axis."),
extraFormula("برآیند دوبعدی","2D resultant","R = √(ΣFx² + ΣFy²)","اندازه برآیند از جمع مؤلفه‌ها به دست می‌آید.","Magnitude from summed components."),
extraFormula("زاویه برآیند","Resultant direction","θ = atan2(ΣFy, ΣFx)","تابع atan2 ربع درست را تعیین می‌کند.","atan2 returns the correct quadrant."),
extraFormula("گشتاور برداری","Vector moment","M_O = r × F","در سه‌بعد، گشتاور حاصل‌ضرب برداری مکان و نیروست.","In 3D, moment is the cross product of position and force."),
extraFormula("گشتاور زوج نیرو","Couple moment","M = F d","d فاصله عمود بین دو خط اثر نیروهای برابر و مخالف است.","d is the perpendicular separation of equal and opposite forces."),
extraFormula("قضیه وارینیون","Varignon’s theorem","M_O(R) = Σ M_O(F_i)","گشتاور برآیند برابر مجموع گشتاور تک‌تک نیروهاست.","Moment of the resultant equals the sum of individual moments."),
extraFormula("اصطکاک جنبشی","Kinetic friction","F_k = μ_k N","برای لغزش؛ جهت نیرو مخالف حرکت نسبی است.","For sliding; friction opposes relative motion."),
extraFormula("زاویه اصطکاک","Angle of friction","tanφ = μ_s","φ زاویه اصطکاک در آستانه لغزش است.","φ is the friction angle at impending slip."),
extraFormula("مرکز سطح مرکب","Composite-area centroid","x̄ = Σ(A_i x_i)/ΣA_i ; ȳ = Σ(A_i y_i)/ΣA_i","برای سوراخ‌ها، مساحت را با علامت منفی وارد کنید.","Use negative area for holes."),
extraFormula("ممان دوم سطح مستطیل","Rectangle second moment of area","I_x = b h³/12","محور از مرکز سطح و موازی قاعده است.","Centroidal axis parallel to the base."),
extraFormula("ممان قطبی سطح دایره","Polar area moment of circle","J = π d⁴/32","برای مقطع دایره‌ای توپر حول مرکز.","For a solid circular section about its center."),
extraFormula("قضیه محورهای موازی سطح","Parallel-axis theorem","I = I_c + A d²","d فاصله دو محور موازی است.","d is the spacing between parallel axes.")
)
"strength" -> listOf(
extraFormula("تغییر طول محوری","Axial deformation","δ = PL/(AE)","برای میله منشوری با رفتار الاستیک خطی.","For a prismatic bar in linear elasticity."),
extraFormula("تنش حرارتی مقید","Restrained thermal stress","σ_th = E α ΔT","برای انبساط کاملاً مقید و بدون تنش اولیه.","For fully restrained thermal expansion."),
extraFormula("پیچش محور دایره‌ای","Shaft torsion","τ = Tr/J","تنش برشی در شعاع r؛ بیشینه در سطح خارجی.","Shear stress at radius r; maximum at the outer surface."),
extraFormula("زاویه پیچش","Angle of twist","θ = TL/(JG)","برای محور منشوری با گشتاور ثابت.","For a prismatic shaft under constant torque."),
extraFormula("تنش برشی تیر","Beam transverse shear","τ = VQ/(It)","Q ممان اول سطح، I ممان دوم سطح و t ضخامت موضعی است.","Q is first moment of area, I second moment, t local thickness."),
extraFormula("رابطه بار، برش و لنگر","Load-shear-moment relations","dV/dx = −w ; dM/dx = V","علامت‌ها تابع قرارداد علامت انتخابی‌اند.","Signs depend on the adopted convention."),
extraFormula("تنش‌های اصلی دوبعدی","Plane-stress principal stresses","σ₁,₂ = (σx+σy)/2 ± √[((σx−σy)/2)²+τxy²]","تنش‌های برشی روی صفحات اصلی صفرند.","Shear stress is zero on principal planes."),
extraFormula("تنش فون‌مایزز دوبعدی","Von Mises stress (plane stress)","σ_vm = √(σx² − σxσy + σy² + 3τxy²)","معیار انرژی اعوجاج برای مواد شکل‌پذیر.","Distortion-energy criterion for ductile materials."),
extraFormula("کمانش اویلر","Euler buckling load","P_cr = π²EI/(KL)²","برای ستون بلند و الاستیک؛ K ضریب طول مؤثر است.","For a long elastic column; K is the effective-length factor."),
extraFormula("ضریب لاغری","Slenderness ratio","λ = KL/r_g","r_g شعاع ژیراسیون مقطع است.","r_g is the radius of gyration."),
extraFormula("انرژی کرنشی محوری","Axial strain energy","U = P²L/(2AE)","برای بار محوری ثابت و رفتار خطی.","For constant axial load and linear behavior."),
extraFormula("ضریب پواسون","Poisson ratio","ν = −ε_lat/ε_long","نسبت کرنش جانبی به طولی با علامت منفی.","Negative lateral-to-longitudinal strain ratio.")
)
"thermo" -> listOf(
extraFormula("معادله انرژی جریان پایا","Steady-flow energy equation","q − w = Δh + Δ(V²/2) + gΔz","کمیت‌ها بر واحد جرم؛ یک ورودی و یک خروجی.","Per unit mass; one inlet and one outlet."),
extraFormula("معادله پیوستگی جرمی","Mass conservation","ṁ = ρAV","برای جریان یک‌بعدی در مقطع.","For one-dimensional flow through a section."),
extraFormula("ظرفیت گرمایی گاز ایده‌آل","Ideal-gas heat capacities","c_p − c_v = R","برای گاز ایده‌آل.","For an ideal gas."),
extraFormula("انرژی داخلی گاز ایده‌آل","Ideal-gas internal energy","Δu = c_v ΔT","با فرض گرمای ویژه ثابت در بازه دما.","Assuming constant specific heat over the temperature range."),
extraFormula("آنتالپی گاز ایده‌آل","Ideal-gas enthalpy","Δh = c_p ΔT","با فرض گرمای ویژه ثابت.","Assuming constant specific heat."),
extraFormula("بازده حرارتی","Thermal efficiency","η_th = W_net,out/Q_in","نسبت کار خالص خروجی به گرمای ورودی.","Net work output divided by heat input."),
extraFormula("ضریب عملکرد یخچال","Refrigerator COP","COP_R = Q_L/W_net,in","گرمای گرفته‌شده از فضای سرد بر کار ورودی.","Heat removed from the cold space over work input."),
extraFormula("ضریب عملکرد پمپ گرما","Heat-pump COP","COP_HP = Q_H/W_net,in = COP_R + 1","برای چرخه بین دو مخزن حرارتی.","For a cycle between two thermal reservoirs."),
extraFormula("حداکثر بازده کارنو","Carnot efficiency","η_C = 1 − T_L/T_H","دماها باید بر حسب کلوین باشند.","Temperatures must be in kelvin."),
extraFormula("آنتروپی گاز ایده‌آل","Ideal-gas entropy change","Δs = c_p ln(T₂/T₁) − R ln(P₂/P₁)","برای گرمای ویژه ثابت و گاز ایده‌آل.","For constant specific heats and an ideal gas."),
extraFormula("رابطه ایزنتروپیک گاز ایده‌آل","Isentropic ideal-gas relation","T₂/T₁ = (P₂/P₁)^((k−1)/k)","فرایند برگشت‌پذیر بی‌دررو؛ k=cp/cv.","Reversible adiabatic process; k=cp/cv."),
extraFormula("کار فرایند هم‌فشار","Constant-pressure boundary work","W = P(V₂−V₁)","کار مرزی برای فشار ثابت.","Boundary work at constant pressure."),
extraFormula("کار پلی‌تروپیک","Polytropic work","W = (P₂V₂−P₁V₁)/(1−n)","برای PVⁿ ثابت و n≠1؛ حالت n=1 لگاریتمی است.","For PV^n constant and n≠1; n=1 is logarithmic."),
extraFormula("خواص مخلوط اشباع","Saturated-mixture property","y = y_f + x y_fg","برای y مانند v، u، h یا s.","For properties such as v, u, h or s."),
extraFormula("گرمای نهان تبخیر","Latent heat of vaporization","h_fg = h_g − h_f","اختلاف آنتالپی بخار و مایع اشباع در یک فشار.","Difference between saturated-vapor and saturated-liquid enthalpy at a pressure."),
extraFormula("بازده ایزنتروپیک توربین","Turbine isentropic efficiency","η_t = (h₁−h₂)/(h₁−h₂s)","خروجی واقعی نسبت به خروجی ایزنتروپیک.","Actual enthalpy drop divided by isentropic enthalpy drop."),
extraFormula("بازده ایزنتروپیک کمپرسور","Compressor isentropic efficiency","η_c = (h₂s−h₁)/(h₂−h₁)","کار ورودی ایزنتروپیک بر کار ورودی واقعی.","Isentropic work input over actual work input."),
extraFormula("چرخه اتو","Otto-cycle efficiency","η_Otto = 1 − 1/r^(k−1)","مدل هوای استاندارد با گرمای ویژه ثابت؛ r نسبت تراکم است.","Air-standard model with constant specific heats; r is compression ratio."),
extraFormula("چرخه برایتون","Brayton-cycle efficiency","η_Brayton = 1 − 1/r_p^((k−1)/k)","مدل ایده‌آل با cp ثابت؛ r_p نسبت فشار کمپرسور است.","Ideal model with constant cp; r_p is compressor pressure ratio.")
)
"dynamics" -> listOf(
extraFormula("سرعت در شتاب ثابت","Velocity with constant acceleration","v = v₀ + at","برای حرکت خطی با شتاب ثابت.","For rectilinear motion with constant acceleration."),
extraFormula("مکان در شتاب ثابت","Position with constant acceleration","x = x₀ + v₀t + ½at²","برای شتاب ثابت.","For constant acceleration."),
extraFormula("رابطه مستقل از زمان","Time-independent kinematics","v² = v₀² + 2a(x−x₀)","برای شتاب ثابت و حرکت خطی.","For constant-acceleration rectilinear motion."),
extraFormula("شتاب مماسی و نرمال","Tangential and normal acceleration","a = √(a_t²+a_n²) ; a_n = v²/ρ","ρ شعاع انحنای مسیر است.","ρ is the radius of curvature."),
extraFormula("مختصات قطبی؛ سرعت","Polar-coordinate velocity","v = ṙ e_r + r θ̇ e_θ","برای حرکت صفحه‌ای در مختصات قطبی.","For planar motion in polar coordinates."),
extraFormula("مختصات قطبی؛ شتاب","Polar-coordinate acceleration","a = (r̈−rθ̇²)e_r + (rθ̈+2ṙθ̇)e_θ","مولفه‌های شعاعی و عرضی.","Radial and transverse components."),
extraFormula("کار و انرژی جنبشی","Work-kinetic energy","U₁→₂ = T₂−T₁","کار خالص نیروها برابر تغییر انرژی جنبشی است.","Net work equals the change in kinetic energy."),
extraFormula("توان لحظه‌ای","Instantaneous power","P = F·v = Mω","حاصل‌ضرب اسکالر نیرو و سرعت یا گشتاور و سرعت زاویه‌ای.","Dot product of force and velocity, or torque and angular speed."),
extraFormula("ضربه و تکانه","Impulse-momentum","∫F dt = m v₂ − m v₁","برای جرم ثابت؛ فرم برداری.","For constant mass; vector form."),
extraFormula("تکانه زاویه‌ای جسم صلب","Rigid-body angular momentum","H_G = I_G ω","برای دوران صفحه‌ای حول مرکز جرم.","For planar rotation about the mass center."),
extraFormula("معادله دوران حول مرکز جرم","Rotation about mass center","ΣM_G = I_G α","برای جسم صلب در حرکت صفحه‌ای.","For a rigid body in planar motion."),
extraFormula("انرژی جنبشی جسم صلب","Rigid-body kinetic energy","T = ½m v_G² + ½I_Gω²","جمع انرژی انتقالی مرکز جرم و دورانی حول مرکز جرم.","Translation of mass center plus rotation about the mass center.")
)
"fluids" -> listOf(
extraFormula("دبی جرمی","Mass flow rate","ṁ = ρAV","چگالی ضربدر سرعت متوسط و سطح مقطع.","Density times average speed and area."),
extraFormula("عدد رینولدز","Reynolds number","Re = ρVD/μ = VD/ν","برای جریان داخلی در لوله، D قطر هیدرولیکی/لوله است.","For internal pipe flow, D is pipe or hydraulic diameter."),
extraFormula("قانون ویسکوزیته نیوتن","Newton’s law of viscosity","τ = μ du/dy","برای سیال نیوتنی در برش ساده.","For a Newtonian fluid in simple shear."),
extraFormula("معادله مومنتوم کنترل‌حجم","Control-volume momentum","ΣF = ṁ(V_out − V_in)","فرم ساده پایا، یک ورودی و یک خروجی؛ برداری.","Simplified steady one-inlet, one-outlet vector form."),
extraFormula("نیروی هیدرواستاتیک سطح تخت","Hydrostatic force on a plane","F_R = ρ g h_c A","h_c عمق مرکز سطح از سطح آزاد است.","h_c is centroid depth below the free surface."),
extraFormula("افت هد دارسی-ویسباخ","Darcy–Weisbach head loss","h_f = f (L/D) V²/(2g)","f ضریب اصطکاک دارسی است؛ با فنینگ اشتباه نشود.","f is the Darcy friction factor; do not confuse with Fanning factor."),
extraFormula("افت موضعی","Minor head loss","h_m = K V²/(2g)","K ضریب افت اتصال یا شیر است.","K is the fitting or valve loss coefficient."),
extraFormula("توان هیدرولیکی پمپ","Hydraulic pump power","P_h = ρ g Q H","توان منتقل‌شده به سیال؛ توان ورودی بیشتر و برابر P_h/η است.","Power delivered to fluid; input power is P_h/η."),
extraFormula("ضریب فشار","Pressure coefficient","C_p = (p−p∞)/(½ρV∞²)","بی‌بعد و پرکاربرد در آیرودینامیک.","Dimensionless coefficient used in aerodynamics."),
extraFormula("نیروی پسا","Drag force","F_D = ½ ρ V² C_D A","A سطح مرجع و CD ضریب پساست.","A is reference area and CD is drag coefficient."),
extraFormula("عدد فرود","Froude number","Fr = V/√(gL)","اهمیت اثر گرانش در جریان‌های سطح آزاد.","Compares inertial and gravitational effects in free-surface flows."),
extraFormula("فشار مطلق","Absolute pressure","p_abs = p_gauge + p_atm","فشار گیج نسبت به فشار اتمسفر سنجیده می‌شود.","Gauge pressure is measured relative to atmospheric pressure."),
extraFormula("معادله برنولی به فرم هد","Bernoulli head equation","p/(ρg) + V²/(2g) + z = constant","برای جریان پایا، تراکم‌ناپذیر و بی‌اتلاف در امتداد خط جریان.","Steady, incompressible, inviscid flow along a streamline.")
)
"heat" -> listOf(
extraFormula("مقاومت رسانش دیواره تخت","Plane-wall conduction resistance","R_cond = L/(kA)","برای رسانش یک‌بعدی پایا؛ Q̇=ΔT/R.","For steady 1D conduction; Q̇=ΔT/R."),
extraFormula("مقاومت رسانش استوانه","Cylindrical conduction resistance","R_cond = ln(r₂/r₁)/(2πkL)","برای دیواره استوانه‌ای هم‌مرکز.","For a concentric cylindrical wall."),
extraFormula("مقاومت جابه‌جایی","Convection resistance","R_conv = 1/(hA)","Q̇=ΔT/R_conv.","Q̇=ΔT/R_conv."),
extraFormula("رسانش چندلایه سری","Composite-wall resistance","R_total = ΣR_i","مقاومت‌های حرارتی سری جمع می‌شوند.","Thermal resistances in series add."),
extraFormula("پخشایی حرارتی","Thermal diffusivity","α = k/(ρ c_p)","نسبت توانایی رسانش به ظرفیت ذخیره انرژی حجمی.","Ratio of heat conduction capability to volumetric heat capacity."),
extraFormula("عدد بیو","Biot number","Bi = h L_c/k_s","برای اعتبار روش ظرفیت متمرکز معمولاً Bi<0.1 لازم است.","Lumped-capacitance approximation is usually valid for Bi<0.1."),
extraFormula("عدد فوریه","Fourier number","Fo = α t/L_c²","زمان بی‌بعد در رسانش گذرا.","Dimensionless time in transient conduction."),
extraFormula("روش ظرفیت متمرکز","Lumped-capacitance temperature","(T−T∞)/(T_i−T∞) = exp(−hAt/(ρVc_p))","وقتی Bi کوچک باشد و دمای جسم تقریباً یکنواخت بماند.","When Bi is small and the body temperature is nearly uniform."),
extraFormula("اختلاف دمای میانگین لگاریتمی","Log-mean temperature difference","ΔT_lm = (ΔT₁−ΔT₂)/ln(ΔT₁/ΔT₂)","در مبدل حرارتی با اختلاف دمای دو انتها.","For heat exchangers with terminal temperature differences."),
extraFormula("رابطه نرخ انتقال مبدل","Heat-exchanger rate","Q̇ = U A ΔT_lm","U ضریب کلی انتقال حرارت است.","U is the overall heat-transfer coefficient."),
extraFormula("بازده پره","Fin efficiency","η_f = Q̇_f/(h A_f (T_b−T∞))","نسبت انتقال واقعی پره به حالت پره با دمای پایه در تمام سطح.","Actual fin heat transfer over ideal isothermal-fin transfer."),
extraFormula("شعاع بحرانی عایق استوانه","Critical insulation radius","r_cr = k_ins/h","برای استوانه؛ افزودن عایق تا شعاع بحرانی ممکن است اتلاف را زیاد کند.","For a cylinder; adding insulation up to this radius can increase heat loss."),
extraFormula("رابطه شار رسانش","Fourier heat flux","q'' = −k dT/dx","علامت منفی نشان‌دهنده حرکت گرما به سمت دمای کمتر است.","Negative sign indicates heat flows toward lower temperature."),
extraFormula("رابطه تابش سطح خاکستری","Gray-surface radiation","Q̇ = εσA(T_s⁴−T_sur⁴)","دماها بر حسب کلوین و محیط بزرگ هم‌دما فرض می‌شود.","Temperatures in kelvin; large isothermal surroundings assumed."),
extraFormula("عدد نوسلت","Nusselt number","Nu = hL/k_f","نسبت انتقال جابه‌جایی به رسانش در لایه سیال.","Convection-to-conduction ratio across the fluid layer.")
)
"machine" -> listOf(
extraFormula("سرعت زاویه‌ای","Angular velocity","ω = dθ/dt","نرخ تغییر زاویه بر حسب زمان.","Rate of angular displacement."),
extraFormula("شتاب زاویه‌ای","Angular acceleration","α = dω/dt","نرخ تغییر سرعت زاویه‌ای.","Rate of change of angular velocity."),
extraFormula("رابطه سرعت خطی و زاویه‌ای","Linear-angular velocity","v = ωr","برای نقطه‌ای در فاصله r از محور دوران.","For a point at radius r from the rotation axis."),
extraFormula("رابطه شتاب مماسی","Tangential acceleration","a_t = αr","مولفه مماسی شتاب نقطه.","Tangential component of point acceleration."),
extraFormula("رابطه شتاب نرمال","Normal acceleration","a_n = ω²r","به سمت مرکز انحناست.","Directed toward the center of curvature."),
extraFormula("قانون چرخ‌دنده‌ها","Gear speed ratio","ω₁/ω₂ = −N₂/N₁","برای دو چرخ‌دنده خارجی؛ علامت منفی جهت مخالف را نشان می‌دهد.","For external gears; negative sign indicates opposite rotation."),
extraFormula("مدول چرخ‌دنده","Gear module","m = d/N","قطر گام تقسیم بر تعداد دندانه؛ در واحدهای سازگار.","Pitch diameter divided by tooth count, consistent units."),
extraFormula("درجه آزادی مکانیزم صفحه‌ای","Planar mobility (Gruebler)","M = 3(n−1) − 2J₁ − J₂","برای لینک‌های صلب صفحه‌ای و مفاصل ایده‌آل؛ n شامل زمین است.","For planar rigid links and ideal lower/higher pairs; n includes ground."),
extraFormula("نسبت انتقال تسمه","Belt-drive speed ratio","ω₁/ω₂ ≈ D₂/D₁","بدون لغزش؛ قطرهای گام یا مؤثر به‌کار روند.","No slip; use pitch/effective diameters."),
extraFormula("توان انتقالی","Transmitted power","P = Tω","T بر حسب N·m و ω بر حسب rad/s.","T in N·m and ω in rad/s."),
extraFormula("نیروی گریز از مرکز معادل","Centrifugal force magnitude","F = m r ω²","در چارچوب چرخان؛ جهت شعاعی به بیرون برای نیروی مجازی.","In a rotating frame; fictitious force is radially outward."),
extraFormula("گشتاور اینرسی","Rotational equation of motion","ΣM_G = I_G α","برای دوران صفحه‌ای حول مرکز جرم.","For planar rotation about the mass center."),
extraFormula("انرژی جنبشی دورانی","Rotational kinetic energy","T_rot = ½ I ω²","I ممان جرم حول محور دوران است.","I is mass moment of inertia about the rotation axis.")
)
"design" -> listOf(
extraFormula("تنش میانگین و دامنه خستگی","Fatigue mean and alternating stress","σ_m=(σ_max+σ_min)/2 ; σ_a=(σ_max−σ_min)/2","برای بارگذاری چرخه‌ای نرمال.","For cyclic normal loading."),
extraFormula("معیار گودمن اصلاح‌شده","Modified Goodman criterion","σ_a/S_e + σ_m/S_ut = 1/n","طراحی خستگی با تنش میانگین کششی؛ در عمل باید ضریب اطمینان لحاظ شود.","Fatigue design with tensile mean stress; include factor of safety."),
extraFormula("معیار سودربرگ","Soderberg criterion","σ_a/S_e + σ_m/S_y = 1/n","معمولاً محافظه‌کارانه‌تر از گودمن است.","Usually more conservative than Goodman."),
extraFormula("معیار گربر","Gerber criterion","σ_a/S_e + (σ_m/S_ut)² = 1/n","رابطه سهمی برای مواد شکل‌پذیر؛ قرارداد طراحی را بررسی کنید.","Parabolic relation for ductile materials; check design convention."),
extraFormula("ضریب تمرکز تنش","Stress concentration factor","K_t = σ_max/σ_nom","برای هندسه و بارگذاری مشخص از نمودار یا تحلیل به دست می‌آید.","Obtained from charts or analysis for the geometry and loading."),
extraFormula("حد دوام اصلاح‌شده","Modified endurance limit","S_e = k_a k_b k_c k_d k_e k_f S'_e","ضرایب سطح، اندازه، بار، دما، قابلیت اطمینان و عوامل دیگر.","Surface, size, load, temperature, reliability and other factors."),
extraFormula("سختی فنر مارپیچ","Helical spring rate","k = G d⁴/(8 D³ N_a)","فنر فشاری مارپیچ با سیم گرد؛ D قطر متوسط حلقه است.","Close-coiled round-wire compression spring; D is mean coil diameter."),
extraFormula("تغییر طول فنر","Helical spring deflection","δ = 8 F D³ N_a/(G d⁴)","برای فنر مارپیچ با سیم گرد و فرض‌های استاندارد.","For a round-wire helical spring under standard assumptions."),
extraFormula("تنش برشی فنر","Spring shear stress","τ = K_w 8FD/(πd³)","K_w ضریب وال؛ اثر انحنا و برش مستقیم را لحاظ می‌کند.","K_w is Wahl factor accounting for curvature and direct shear."),
extraFormula("عمر پایه یاتاقان","Bearing basic rating life","L₁₀ = (C/P)^p × 10⁶ rev","p=3 برای ساچمه‌ای و 10/3 برای غلتکی؛ بار معادل P.","p=3 for ball and 10/3 for roller bearings; P is equivalent load."),
extraFormula("قطر گام چرخ‌دنده","Gear pitch diameter","d = m N","m مدول و N تعداد دندانه است.","m is module and N is tooth count."),
extraFormula("تنش خمشی دندانه لوییس","Lewis gear-tooth bending estimate","σ = F_t/(b m Y)","تقریب مقدماتی؛ طراحی واقعی به ضرایب استاندارد و عوامل دینامیکی نیاز دارد.","Preliminary estimate; full design needs standard and dynamic factors."),
extraFormula("تنش برشی خار","Key shear stress","τ = 2T/(d b L)","برای خار مستطیلی با عرض b و طول L در محور قطر d.","For a rectangular key of width b and length L on shaft diameter d."),
extraFormula("تنش لهیدگی خار","Key bearing stress","σ_b = 4T/(d h L)","h ارتفاع خار است؛ بارگذاری یکنواخت فرض شده است.","h is key height; assumes uniform bearing load."),
extraFormula("تنش پیچشی با توان و دور","Shaft torque from power","T = 9550 P_kW/n_rpm","T بر حسب N·m؛ P بر حسب kW و n بر حسب rpm.","T in N·m; P in kW and n in rpm."),
extraFormula("بار بحرانی اویلر","Euler critical column load","P_cr = π²EI/(KL)²","برای ستون بلند الاستیک؛ محدودیت‌های کاربرد بررسی شود.","For long elastic columns; check applicability."),
extraFormula("تنش معادل فون‌مایزز","Von Mises equivalent stress","σ_vm = √(σ_x²+σ_y²−σ_xσ_y+3τ_xy²)","برای حالت تنش صفحه‌ای با مؤلفه‌های مناسب.","For plane stress with the appropriate components."),
extraFormula("توان و گشتاور با دور","Power-torque-speed relation","P_kW = T n/9550","T بر حسب N·m و n بر حسب rpm.","T in N·m and n in rpm."),
extraFormula("عمر یاتاقان بر حسب ساعت","Bearing life in hours","L₁₀h = (10⁶/(60n))(C/P)^p","n دور بر دقیقه است.","n is rotational speed in rpm.")
)
"vibrations" -> listOf(
extraFormula("معادله حرکت سیستم یک درجه آزادی","Single-DOF equation of motion","m ẍ + c ẋ + kx = F(t)","جرم، میرایی، سختی و نیروی تحریک.","Mass, damping, stiffness and excitation."),
extraFormula("فرکانس طبیعی میرا","Damped natural frequency","ω_d = ω_n √(1−ζ²)","برای سیستم کم‌میرای ζ<1.","For an underdamped system, ζ<1."),
extraFormula("نسبت میرایی بحرانی","Critical damping ratio","ζ = c/(2√(km))","ζ بدون بعد است.","Dimensionless damping ratio."),
extraFormula("کاهش لگاریتمی","Logarithmic decrement","δ = ln(x_i/x_{i+1}) = 2πζ/√(1−ζ²)","دامنه‌های متوالی هم‌علامت در ارتعاش آزاد کم‌میرا.","Successive same-side peaks in free underdamped vibration."),
extraFormula("بزرگنمایی دینامیکی","Dynamic magnification factor","M = 1/√[(1−r²)²+(2ζr)²]","r=ω/ω_n؛ پاسخ هارمونیک پایا.","r=ω/ω_n; steady harmonic response."),
extraFormula("نسبت انتقال ارتعاش","Displacement transmissibility","T_R = √[(1+(2ζr)²)/((1−r²)²+(2ζr)²)]","برای تحریک پایه و مدل یک درجه آزادی.","For base excitation in a single-DOF model."),
extraFormula("فرکانس طبیعی پیچشی","Torsional natural frequency","ω_n = √(k_t/I)","k_t سختی پیچشی و I ممان جرم دورانی است.","k_t is torsional stiffness and I is mass moment of inertia."),
extraFormula("سختی پیچشی محور","Shaft torsional stiffness","k_t = GJ/L","برای محور یکنواخت.","For a uniform shaft."),
extraFormula("انرژی ارتعاشی سیستم","Vibration energy","E = ½m ẋ² + ½k x²","برای سیستم آزاد بدون میرایی.","For an undamped free system."),
extraFormula("فرکانس طبیعی بر حسب هرتز","Natural frequency in hertz","f_n = ω_n/(2π)","تعداد چرخه در ثانیه.","Cycles per second.")
)
"math" -> listOf(
extraFormula("قاعده زنجیره‌ای","Chain rule","dy/dx = (dy/du)(du/dx)","برای تابع مرکب y(u(x)).","For a composite function y(u(x))."),
extraFormula("مشتق نمایی","Exponential derivative","d(e^x)/dx = e^x","تابع نمایی طبیعی مشتق خودش است.","The natural exponential is its own derivative."),
extraFormula("مشتق لگاریتم طبیعی","Natural logarithm derivative","d(ln x)/dx = 1/x","برای x>0.","For x>0."),
extraFormula("انتگرال نمایی","Exponential integral","∫e^(ax) dx = e^(ax)/a + C","برای a≠0.","For a≠0."),
extraFormula("انتگرال سینوس","Sine integral","∫sin(ax)dx = −cos(ax)/a + C","برای a≠0.","For a≠0."),
extraFormula("مشتق جزئی","Partial derivative","∂f/∂x","سایر متغیرهای مستقل ثابت فرض می‌شوند.","Other independent variables are held constant."),
extraFormula("سری هندسی","Geometric series","Σ_(n=0)^∞ arⁿ = a/(1−r)","برای |r|<1.","For |r|<1."),
extraFormula("معادله مشخصه مرتبه دوم","Second-order characteristic equation","a r² + b r + c = 0","ریشه‌ها رفتار حل معادله خطی را تعیین می‌کنند.","Roots determine the behavior of a linear ODE solution."),
extraFormula("تبدیل لاپلاس مشتق اول","Laplace transform of first derivative","L{f'} = sF(s) − f(0)","برای تابع با تبدیل لاپلاس موجود.","For functions with an existing Laplace transform."),
extraFormula("تبدیل لاپلاس مشتق دوم","Laplace transform of second derivative","L{f''} = s²F(s) − s f(0) − f'(0)","شرایط اولیه در تبدیل ظاهر می‌شوند.","Initial conditions appear in the transform."),
extraFormula("تبدیل لاپلاس نمایی","Laplace transform of exponential","L{e^(at)} = 1/(s−a)","برای s>a.","For s>a."),
extraFormula("سری تیلور","Taylor series","f(x)=Σ f⁽ⁿ⁾(a)(x−a)ⁿ/n!","در بازه همگرایی تابع.","Within the function's interval of convergence.")
)
else -> emptyList()
}

private val lessons = listOf(
 Lesson("statics","استاتیک","Statics","تعادل نیروها و گشتاورها","Force and moment equilibrium",
 listOf(formula("قانون تعادل نیروها","ΣF = 0","برآیند نیروها در حالت تعادل صفر است.","The resultant force is zero in equilibrium."),formula("تعادل گشتاورها","ΣM = 0","مجموع گشتاورها حول هر نقطه صفر است.","The sum of moments about any point is zero."),formula("گشتاور نیرو","M = F d⊥","فاصله عمودی خط اثر نیرو تا نقطه.","Perpendicular distance to the line of action."),formula("اصطکاک حدی","Fₛ,max = μₛ N","حداکثر اصطکاک ایستایی برابر ضریب اصطکاک در نیروی عمودی است.","Maximum static friction.")),
 listOf("جسم صلب در تعادل است اگر برآیند نیروها و گشتاورها صفر باشد." to "A rigid body is in equilibrium when net force and moment are zero.","نمودار جسم آزاد تمام نیروها و گشتاورهای خارجی را نشان می‌دهد." to "A free-body diagram shows all external forces and moments.","واکنش تکیه‌گاه به نوع قید و درجات آزادی محدودشده بستگی دارد." to "Support reactions depend on the constraints."),
 listOf(
 q("نیروی ۱۰ نیوتن با بازوی عمود ۰٫۵ متر چه گشتاوری ایجاد می‌کند؟","A 10 N force acts with a 0.5 m perpendicular arm. Find the moment.","۵ نیوتن‌متر؛ M=10×0.5.","5 N·m; M=10×0.5."),
 q("دو نیروی ۳۰ و ۲۰ نیوتن در یک راستا و خلاف جهت‌اند. برآیند؟","Two collinear forces of 30 N and 20 N act oppositely. Resultant?","۱۰ نیوتن در جهت نیروی ۳۰ نیوتنی.","10 N in the direction of the 30 N force."),
 q("جسمی با سه نیروی همگرا در تعادل است. برآیند نیروها؟","A body under three concurrent forces is in equilibrium. Net force?","صفر؛ مجموع مؤلفه‌های x و y باید صفر باشد.","Zero; sums of x and y components must vanish."),
 q("گشتاور نیرویی که خط اثرش از نقطه دوران می‌گذرد چقدر است؟","What is the moment when the force line passes through the pivot?","صفر؛ بازوی عمود صفر است.","Zero; the perpendicular lever arm is zero."),
 q("اگر μs=0.4 و N=100 N باشد، اصطکاک ایستایی بیشینه؟","If μs=0.4 and N=100 N, maximum static friction?","۴۰ نیوتن.","40 N."),
 q("واکنش یک تکیه‌گاه غلتکی روی سطح افقی در چه جهتی است؟","Direction of a roller support reaction on a horizontal surface?","عمود بر سطح؛ در این حالت قائم.","Normal to the surface; vertical here."),
 q("مولفه افقی نیروی ۵۰ نیوتن با زاویه ۶۰ درجه نسبت به افق؟","Horizontal component of 50 N at 60° to horizontal?","۲۵ نیوتن؛ 50cos60°.","25 N; 50cos60°."),
 q("برای تعادل صفحه‌ای چند معادله مستقل تعادل داریم؟","How many independent equilibrium equations exist in 2D?","سه معادله: ΣFx=0، ΣFy=0 و ΣM=0.","Three: ΣFx=0, ΣFy=0 and ΣM=0."),
 q("اگر برآیند نیرو صفر ولی گشتاور غیرصفر باشد، وضعیت چیست؟","If net force is zero but net moment is nonzero, what happens?","جسم شتاب خطی ندارد اما شتاب زاویه‌ای می‌گیرد.","No translational acceleration, but angular acceleration occurs."),
 q("مرکز جرم یک صفحه یکنواخت مستطیلی کجاست؟","Where is the centroid of a uniform rectangular plate?","در محل تقاطع قطرها، یعنی وسط طول و عرض.","At the intersection of diagonals, midway along length and width.")
 )),
 Lesson("strength","مقاومت مصالح","Strength of Materials","تنش، کرنش و تغییر شکل","Stress, strain and deformation",
 listOf(formula("تنش نرمال","σ = P/A","نیروی محوری تقسیم بر سطح مقطع.","Axial load divided by cross-sectional area."),formula("کرنش محوری","ε = δ/L","تغییر طول تقسیم بر طول اولیه.","Change in length over original length."),formula("قانون هوک","σ = E ε","در محدوده الاستیک خطی.","Within the linear elastic range."),formula("تنش برشی متوسط","τ = V/A","نیروی برشی تقسیم بر سطح برش.","Shear force over shear area."),formula("خمش تیر","σ = My/I","تنش خمشی در فاصله y از محور خنثی.","Bending stress at distance y from neutral axis.")),
 listOf("تنش شدت نیروی داخلی بر واحد سطح است." to "Stress is internal force intensity per unit area.","کرنش نسبت تغییر طول به طول اولیه است و بی‌بعد است." to "Strain is relative deformation and is dimensionless.","مدول یانگ شیب نمودار تنش-کرنش در ناحیه الاستیک خطی است." to "Young's modulus is the linear-elastic stress-strain slope."),
 listOf(
 q("میله‌ای با P=20 kN و A=200 mm² چه تنشی دارد؟","A bar has P=20 kN and A=200 mm². Find stress.","۱۰۰ مگاپاسکال؛ 20000/200=100 N/mm².","100 MPa; 20000/200=100 N/mm²."),
 q("میله ۲ متری یک میلی‌متر کشیده می‌شود. کرنش؟","A 2 m bar elongates by 1 mm. Strain?","۰٫۰۰۰۵؛ 0.001/2.","0.0005; 0.001/2."),
 q("اگر σ=100 MPa و E=200 GPa باشد، کرنش؟","If σ=100 MPa and E=200 GPa, find strain.","۰٫۰۰۰۵؛ ε=σ/E.","0.0005; ε=σ/E."),
 q("واحد تنش در SI چیست؟","SI unit of stress?","پاسکال؛ معادل نیوتن بر مترمربع.","Pascal; equivalent to N/m²."),
 q("در خمش خالص، تنش در محور خنثی چقدر است؟","In pure bending, what is stress at the neutral axis?","صفر؛ چون y=0 است.","Zero because y=0."),
 q("اگر قطر میلگرد دو برابر شود، سطح مقطع چند برابر می‌شود؟","If a rod diameter doubles, how does area change?","چهار برابر؛ A متناسب با d² است.","Four times; area is proportional to d²."),
 q("تنش برشی متوسط برای V=12 kN و A=300 mm²؟","Average shear stress for V=12 kN and A=300 mm²?","۴۰ مگاپاسکال.","40 MPa."),
 q("ضریب پواسون نسبت کدام کرنش‌هاست؟","Poisson's ratio relates which strains?","منفی نسبت کرنش جانبی به کرنش طولی.","Negative lateral strain divided by longitudinal strain."),
 q("در ماده شکل‌پذیر، تغییر شکل دائمی از چه ناحیه‌ای آغاز می‌شود؟","In a ductile material, permanent deformation begins at which point?","پس از عبور از حد تسلیم.","After exceeding the yield point."),
 q("اگر طول اولیه ثابت و سطح مقطع بیشتر شود، تنش محوری تحت بار ثابت چه می‌شود؟","With fixed length and load, what happens to axial stress if area increases?","کاهش می‌یابد؛ σ=P/A.","It decreases; σ=P/A.")
 )),
 Lesson("thermo","ترمودینامیک ۱","Thermodynamics I","خواص ماده، قانون اول و چرخه‌ها","Properties, first law and cycles",
 listOf(formula("قانون اول سیستم بسته","ΔU = Q − W","گرمای ورودی منهای کار خروجی.","Heat into the system minus work out."),formula("گاز ایده‌آل","PV = mRT","رابطه فشار، حجم، جرم و دما.","Pressure-volume ideal gas relation."),formula("کار مرزی","W = ∫P dV","کار در فرایند شبه‌تعادلی.","Boundary work in a quasi-equilibrium process."),formula("آنتالپی","h = u + Pv","انرژی داخلی ویژه به‌علاوه انرژی جریانی.","Specific internal plus flow energy."),formula("کیفیت بخار","x = mᵥ/m","نسبت جرم بخار به جرم کل مخلوط اشباع.","Vapor mass fraction in a saturated mixture.")),
 listOf("سیستم مقدار مشخصی از ماده یا ناحیه‌ای برای بررسی است." to "A system is a fixed quantity of matter or a region under study.","خاصیت شدتی به جرم وابسته نیست؛ مثل دما و فشار." to "Intensive properties do not depend on mass, e.g. temperature and pressure.","در مخلوط اشباع، کیفیت بین صفر و یک است." to "For a saturated mixture, quality lies between zero and one.","فرایند هم‌حجم یعنی حجم ثابت و کار مرزی صفر است." to "At constant volume, boundary work is zero."),
 listOf(
 q("گاز ۲ kJ گرما می‌گیرد و ۰٫۵ kJ کار انجام می‌دهد. ΔU؟","Gas receives 2 kJ heat and performs 0.5 kJ work. Find ΔU.","۱٫۵ کیلوژول؛ ΔU=Q−W.","1.5 kJ; ΔU=Q−W."),
 q("در فرایند حجم ثابت، کار مرزی چقدر است؟","Boundary work in a constant-volume process?","صفر؛ چون dV=0.","Zero because dV=0."),
 q("کیفیت مخلوط اشباع با mᵥ=2 kg و mₗ=3 kg؟","Quality if vapor mass is 2 kg and liquid mass is 3 kg?","۰٫۴؛ 2/(2+3).","0.4; 2/(2+3)."),
 q("گاز ایده‌آل در دمای ثابت فشرده شود، فشار چه تغییری می‌کند؟","An ideal gas is compressed isothermally. What happens to pressure?","افزایش می‌یابد؛ طبق PV ثابت.","It increases; PV remains constant."),
 q("واحد ثابت گاز ویژه R در SI چیست؟","SI unit of specific gas constant R?","ژول بر کیلوگرم کلوین.","J/(kg·K)."),
 q("اگر انرژی داخلی ۸ kJ افزایش یابد و کار خروجی ۳ kJ باشد، گرما؟","If internal energy rises by 8 kJ and output work is 3 kJ, find heat.","۱۱ کیلوژول؛ Q=ΔU+W.","11 kJ; Q=ΔU+W."),
 q("در نقطه سه‌گانه چند فاز در تعادل‌اند؟","How many phases coexist at the triple point?","سه فاز: جامد، مایع و بخار.","Three: solid, liquid and vapor."),
 q("آنتالپی ویژه از چه کمیت‌هایی تشکیل می‌شود؟","Which properties define specific enthalpy?","h=u+Pv.","h=u+Pv."),
 q("اگر کیفیت صفر باشد، حالت ماده چیست؟","What is the state when quality is zero?","مایع اشباع.","Saturated liquid."),
 q("دمای اشباع آب با فشار افزایش یابد، چه می‌شود؟","What happens to water saturation temperature as pressure rises?","دمای اشباع افزایش می‌یابد.","Saturation temperature increases.")
 )),
 Lesson("dynamics","دینامیک","Engineering Dynamics","حرکت‌شناسی و سینتیک ذرات و اجسام صلب","Kinematics and kinetics",
 listOf(formula("سرعت متوسط","v̄ = Δx/Δt","تغییر مکان بر زمان.","Displacement per time."),formula("شتاب","a = dv/dt","نرخ تغییر سرعت.","Rate of change of velocity."),formula("حرکت با شتاب ثابت","v = v₀ + at","رابطه سرعت و زمان.","Velocity-time relation."),formula("قانون دوم نیوتن","ΣF = ma","برآیند نیرو برابر جرم در شتاب.","Net force equals mass times acceleration."),formula("انرژی جنبشی","T = ½mv²","انرژی ناشی از حرکت.","Energy due to motion.")),
 listOf("حرکت‌شناسی حرکت را بدون بررسی علت آن توصیف می‌کند." to "Kinematics describes motion without its causes.","سینتیک ارتباط نیروها و حرکت را بررسی می‌کند." to "Kinetics relates forces to motion.","شتاب مماسی اندازه سرعت را تغییر می‌دهد و شتاب عمودی جهت سرعت را." to "Tangential acceleration changes speed; normal acceleration changes direction."),
 listOf(
 q("جسمی از سکون با شتاب ۲ m/s² به مدت ۵ s حرکت می‌کند. سرعت نهایی؟","A body starts from rest with 2 m/s² for 5 s. Final speed?","۱۰ متر بر ثانیه؛ v=0+2×5.","10 m/s; v=0+2×5."),
 q("در حرکت دایره‌ای با v=4 m/s و r=2 m، شتاب مرکزگرا؟","Circular motion with v=4 m/s and r=2 m. Centripetal acceleration?","۸ متر بر مجذور ثانیه؛ v²/r.","8 m/s²; v²/r."),
 q("تکانه خطی جسم ۳ kg با سرعت ۴ m/s؟","Linear momentum of a 3 kg body at 4 m/s?","۱۲ kg·m/s؛ p=mv.","12 kg·m/s; p=mv."),
 q("کار نیروی ثابت ۱۰ N در جابه‌جایی ۳ m هم‌جهت؟","Work by 10 N over 3 m in the same direction?","۳۰ ژول.","30 J."),
 q("اگر نیروی خالص صفر باشد، شتاب انتقالی؟","If net force is zero, translational acceleration?","صفر؛ طبق قانون دوم نیوتن.","Zero by Newton's second law."),
 q("سرعت زاویه‌ای ۲ rad/s و شعاع ۰٫۵ m؛ سرعت خطی؟","Angular speed 2 rad/s, radius 0.5 m. Linear speed?","۱ متر بر ثانیه؛ v=ωr.","1 m/s; v=ωr."),
 q("انرژی جنبشی جرم ۲ kg با سرعت ۳ m/s؟","Kinetic energy of 2 kg at 3 m/s?","۹ ژول؛ ½×2×9.","9 J; ½×2×9."),
 q("ضربه برابر تغییر کدام کمیت است؟","Impulse equals change in which quantity?","تکانه خطی.","Linear momentum."),
 q("در پرتابه بدون مقاومت هوا، شتاب افقی چقدر است؟","For projectile motion without air resistance, horizontal acceleration?","صفر.","Zero."),
 q("اگر سرعت ثابت باشد، شتاب چیست؟","If velocity is constant, what is acceleration?","صفر؛ اندازه و جهت سرعت تغییر نمی‌کند.","Zero; velocity magnitude and direction do not change.")
 )),
 Lesson("fluids","مکانیک سیالات","Fluid Mechanics","فشار، شناوری و جریان سیال","Pressure, buoyancy and fluid flow",
 listOf(formula("فشار","p = F/A","نیروی عمود بر واحد سطح.","Normal force per unit area."),formula("فشار هیدرواستاتیک","p = ρgh","فشار ناشی از ستون سیال.","Pressure due to a fluid column."),formula("معادله پیوستگی","A₁V₁ = A₂V₂","برای جریان پایا و تراکم‌ناپذیر.","Steady incompressible flow."),formula("برنولی","p + ½ρV² + ρgz = ثابت","در امتداد خط جریان با فرض‌های استاندارد.","Along a streamline under standard assumptions."),formula("نیروی شناوری","Fᵦ = ρgV","وزن سیال جابه‌جا‌شده.","Weight of displaced fluid.")),
 listOf("سیال در سکون تنش برشی پایدار تحمل نمی‌کند." to "A fluid at rest cannot sustain shear stress.","فشار در یک نقطه سیال ساکن در همه جهات یکسان است." to "Static pressure at a point is isotropic.","عدد رینولدز نسبت نیروهای لختی به لزجت است." to "Reynolds number compares inertial and viscous effects."),
 listOf(
 q("فشار آب در عمق ۲ m با ρ=1000 kg/m³ و g=10؟","Water pressure at 2 m depth, ρ=1000 kg/m³ and g=10?","۲۰۰۰۰ پاسکال؛ ρgh.","20,000 Pa; ρgh."),
 q("مساحت لوله نصف شود، سرعت در جریان تراکم‌ناپذیر چه می‌شود؟","Pipe area is halved in incompressible flow. What happens to speed?","دو برابر می‌شود؛ از معادله پیوستگی.","It doubles by continuity."),
 q("نیروی شناوری به چه چیزی برابر است؟","Buoyant force equals what?","وزن سیال جابه‌جا‌شده.","Weight of displaced fluid."),
 q("واحد ویسکوزیته دینامیکی در SI چیست؟","SI unit of dynamic viscosity?","پاسکال‌ثانیه (Pa·s).","Pascal-second (Pa·s)."),
 q("اگر عدد رینولدز کم باشد، جریان معمولاً چه نوعی است؟","At low Reynolds number, flow is usually what type?","آرام یا لامینار.","Laminar."),
 q("فشار مطلق از چه رابطه‌ای به دست می‌آید؟","How is absolute pressure calculated?","فشار گیج به‌علاوه فشار اتمسفر.","Gauge pressure plus atmospheric pressure."),
 q("در معادله برنولی افزایش سرعت معمولاً با فشار استاتیک چه رابطه‌ای دارد؟","In Bernoulli flow, increased speed is generally associated with what static-pressure change?","در ارتفاع ثابت و شرایط ایده‌آل، فشار استاتیک کاهش می‌یابد.","At constant elevation ideally, static pressure decreases."),
 q("دبی حجمی برابر چیست؟","Volumetric flow rate equals what?","Q=AV.","Q=AV."),
 q("چگالی نسبی چیست؟","What is specific gravity?","نسبت چگالی ماده به چگالی مرجع، معمولاً آب.","Density ratio to a reference, usually water."),
 q("فشار در کف مخزن به چه عواملی وابسته است؟","What determines pressure at a tank bottom?","چگالی سیال، شتاب گرانش و عمق؛ نه شکل مخزن.","Fluid density, gravity and depth, not tank shape.")
 )),
 Lesson("heat","انتقال حرارت","Heat Transfer","رسانش، جابه‌جایی و تابش","Conduction, convection and radiation",
 listOf(formula("قانون فوریه","Q̇ = −kA dT/dx","شار رسانش در راستای کاهش دما.","Conduction rate down the temperature gradient."),formula("جابه‌جایی نیوتن","Q̇ = hA(Tₛ−T∞)","انتقال حرارت بین سطح و سیال.","Heat transfer between a surface and fluid."),formula("قانون استفان-بولتزمن","Q̇ = εσA(Tₛ⁴−Tₛurr⁴)","تابش خالص سطح خاکستری به محیط بزرگ.","Net radiation from a gray surface to large surroundings."),formula("مقاومت رسانش دیواره","R = L/(kA)","مقاومت حرارتی دیواره تخت.","Thermal resistance of a plane wall.")),
 listOf("رسانش انتقال انرژی از طریق برهم‌کنش مولکولی یا الکترونی است." to "Conduction transfers energy through molecular or electronic interactions.","جابه‌جایی به حرکت توده‌ای سیال نیاز دارد." to "Convection requires bulk fluid motion.","تابش برای انتقال انرژی به محیط مادی نیاز ندارد." to "Radiation needs no material medium."),
 listOf(
 q("واحد ضریب هدایت حرارتی k چیست؟","SI unit of thermal conductivity k?","وات بر متر کلوین؛ W/(m·K).","W/(m·K)."),
 q("با دو برابر شدن ضخامت دیواره، مقاومت رسانش چه می‌شود؟","If wall thickness doubles, what happens to conduction resistance?","دو برابر می‌شود؛ R=L/(kA).","It doubles; R=L/(kA)."),
 q("در قانون نیوتن، اگر اختلاف دما صفر باشد، نرخ جابه‌جایی؟","In Newton's cooling law, if temperature difference is zero, heat rate?","صفر.","Zero."),
 q("کدام مکانیزم انتقال حرارت در خلأ ممکن است؟","Which heat-transfer mode works in vacuum?","تابش.","Radiation."),
 q("عدد ناسلت نسبت کدام انتقال‌ها را بیان می‌کند؟","Nusselt number compares which heat-transfer modes?","جابه‌جایی به رسانش در لایه سیال.","Convection to conduction across the fluid layer."),
 q("اگر سطح انتقال حرارت دو برابر شود و بقیه ثابت باشند، Q̇ جابه‌جایی؟","If area doubles with other terms fixed, convective heat rate?","دو برابر می‌شود.","It doubles."),
 q("واحد ضریب جابه‌جایی h چیست؟","SI unit of convection coefficient h?","W/(m²·K).","W/(m²·K)."),
 q("رسانش پایا در دیواره تخت با تولید حرارت صفر چه شکلی از توزیع دما دارد؟","Steady 1D conduction in a plane wall with no generation gives what temperature profile?","خطی.","Linear."),
 q("گسیلندگی سطح بین چه مقادیری است؟","Emissivity lies between which values?","بین صفر و یک.","Between zero and one."),
 q("مقاومت حرارتی سری چگونه محاسبه می‌شود؟","How are series thermal resistances combined?","با جمع مقاومت‌ها.","By adding the resistances.")
 )),
 Lesson("machine","دینامیک ماشین","Machine Dynamics","سینماتیک مکانیزم‌ها و تحلیل حرکت","Mechanism kinematics and motion analysis",
 listOf(formula("نسبت سرعت زاویه‌ای","i = ωin/ωout","نسبت سرعت ورودی به خروجی.","Input-to-output angular speed ratio."),formula("سرعت نقطه روی جسم صلب","vB = vA + ω×rB/A","رابطه سرعت نسبی نقاط جسم صلب.","Relative velocity of rigid-body points."),formula("شتاب مماسی","aₜ = αr","مولفه مماسی شتاب در حرکت دورانی.","Tangential acceleration in rotation."),formula("شتاب نرمال","aₙ = ω²r = v²/r","مولفه رو به مرکز.","Inward normal acceleration.")),
 listOf("لینک‌ها اعضای صلب مکانیزم‌اند و با مفصل به هم متصل می‌شوند." to "Links are rigid members connected by joints.","درجه آزادی تعداد ورودی‌های مستقل لازم برای تعیین وضعیت مکانیزم است." to "Degrees of freedom count independent inputs needed to define configuration.","نمودار جابه‌جایی-زمان با مشتق‌گیری به سرعت و سپس شتاب می‌رسد." to "Differentiating displacement-time gives velocity then acceleration."),
 listOf(
 q("سرعت زاویه‌ای ۱۰ rad/s و شعاع ۰٫۲ m؛ سرعت خطی؟","Angular speed 10 rad/s and radius 0.2 m. Linear speed?","۲ متر بر ثانیه؛ v=ωr.","2 m/s; v=ωr."),
 q("اگر α=5 rad/s² و r=0.4 m باشد، شتاب مماسی؟","If α=5 rad/s² and r=0.4 m, tangential acceleration?","۲ متر بر مجذور ثانیه.","2 m/s²."),
 q("در حرکت دورانی یکنواخت، شتاب زاویه‌ای چقدر است؟","In uniform rotation, angular acceleration?","صفر.","Zero."),
 q("نسبت سرعت ورودی 1200 rpm به خروجی 300 rpm؟","Input 1200 rpm and output 300 rpm. Speed ratio?","۴.","4."),
 q("اگر سرعت زاویه‌ای دو برابر شود، شتاب نرمال چند برابر می‌شود؟","If angular speed doubles, how does normal acceleration change?","چهار برابر؛ aₙ متناسب با ω² است.","Four times; aₙ is proportional to ω²."),
 q("واحد سرعت زاویه‌ای چیست؟","Unit of angular velocity?","رادیان بر ثانیه.","rad/s."),
 q("در مکانیزم چهارمیله‌ای، کدام عضو معمولاً ثابت است؟","In a four-bar linkage, which link is usually fixed?","لینک زمین یا قاب.","The ground link or frame."),
 q("مشتق جابه‌جایی نسبت به زمان چیست؟","Time derivative of displacement?","سرعت.","Velocity."),
 q("اگر مکانیزم یک درجه آزادی داشته باشد، چند ورودی مستقل لازم است؟","How many independent inputs does a one-DOF mechanism need?","یک ورودی.","One input."),
 q("در چرخ‌دنده‌های درگیر خارجی، جهت دوران خروجی نسبت به ورودی؟","For externally meshing gears, output rotation direction?","خلاف جهت ورودی.","Opposite to the input.")
 )),
 Lesson("design","طراحی اجزا","Machine Design","طراحی محور، اتصالات و اجزای انتقال قدرت","Shafts, joints and power transmission",
 listOf(formula("تنش پیچشی محور","τmax = Tc/J","T گشتاور، c شعاع خارجی و J ممان قطبی.","T torque, c outer radius, J polar moment."),formula("توان دورانی","P = Tω","توان برابر گشتاور در سرعت زاویه‌ای.","Power equals torque times angular speed."),formula("تنش برشی پیچ","τ = F/A","در برآورد ساده تنش برشی متوسط.","Average shear stress estimate."),formula("ضریب اطمینان","n = Sallow/σactual","نسبت مقاومت مجاز به تنش واقعی.","Allowable strength divided by actual stress.")),
 listOf("طراحی باید مقاومت، سختی، عمر و قابلیت ساخت را همزمان در نظر بگیرد." to "Design considers strength, stiffness, life and manufacturability.","خستگی شکست تحت بارگذاری تکراری است و ممکن است زیر تنش تسلیم رخ دهد." to "Fatigue is failure under repeated loading, possibly below yield stress.","تمرکز تنش در ناپیوستگی هندسی مانند شیار و سوراخ رخ می‌دهد." to "Stress concentration occurs at geometric discontinuities."),
 listOf(
 q("گشتاور 100 N·m با سرعت 20 rad/s چه توانی دارد؟","Torque 100 N·m at 20 rad/s gives what power?","۲۰۰۰ وات؛ P=Tω.","2000 W; P=Tω."),
 q("ضریب اطمینان اگر مقاومت مجاز 300 MPa و تنش واقعی 100 MPa باشد؟","Factor of safety for allowable strength 300 MPa and actual stress 100 MPa?","۳.","3."),
 q("هدف اصلی خار در اتصال محور و توپی چیست؟","Main purpose of a key between shaft and hub?","انتقال گشتاور و جلوگیری از لغزش نسبی دورانی.","Transmit torque and prevent relative rotation."),
 q("کدام بارگذاری معمولاً در خستگی اهمیت دارد؟","Which loading is important in fatigue?","بارگذاری متناوب یا چرخه‌ای.","Alternating or cyclic loading."),
 q("واحد گشتاور چیست؟","Unit of torque?","نیوتن‌متر.","N·m."),
 q("در پیچ تحت کشش، افزایش قطر با ثابت بودن نیرو چه اثری بر تنش دارد؟","For a tensile bolt at fixed load, effect of increasing diameter on stress?","تنش کاهش می‌یابد؛ سطح مقطع بیشتر می‌شود.","Stress decreases as area increases."),
 q("یاتاقان چه وظیفه‌ای دارد؟","What is the function of a bearing?","پشتیبانی محور و کاهش اصطکاک حرکت نسبی.","Support a shaft and reduce friction."),
 q("در طراحی، ضریب اطمینان کمتر از یک معمولاً چه مفهومی دارد؟","What does a factor of safety below one usually indicate?","تنش واقعی از مقاومت مبنا بیشتر است؛ طراحی ایمن نیست.","Actual stress exceeds reference strength; design is unsafe."),
 q("کدام عامل تمرکز تنش را افزایش می‌دهد؟","What increases stress concentration?","تغییر ناگهانی مقطع، شیار تیز یا سوراخ.","Abrupt section changes, sharp notches or holes."),
 q("توان 1 kW معادل چند وات است؟","How many watts are in 1 kW?","۱۰۰۰ وات.","1000 W.")
 )),
 Lesson("vibrations","ارتعاشات","Mechanical Vibrations","ارتعاش آزاد، اجباری و میرایی","Free, forced vibration and damping",
 listOf(formula("فرکانس طبیعی جرم-فنر","ωn = √(k/m)","برای سیستم یک درجه آزادی بدون میرایی.","Undamped single-DOF system."),formula("فرکانس","f = ω/(2π)","تعداد چرخه در ثانیه.","Cycles per second."),formula("نسبت میرایی","ζ = c/cc","نسبت میرایی واقعی به بحرانی.","Actual to critical damping."),formula("میرایی بحرانی","cc = 2√(km)","مرز بین پاسخ کم‌میرای نوسانی و بیش‌میرای غیرنوسانی.","Boundary between underdamped and overdamped response.")),
 listOf("ارتعاش آزاد پس از اغتشاش اولیه و بدون نیروی خارجی ادامه می‌یابد." to "Free vibration continues after an initial disturbance without external forcing.","رزونانس نزدیک شدن فرکانس تحریک به فرکانس طبیعی است." to "Resonance occurs when excitation approaches natural frequency.","میرایی انرژی ارتعاش را تلف می‌کند." to "Damping dissipates vibrational energy."),
 listOf(
 q("جرم ۲ kg و سختی 200 N/m؛ فرکانس طبیعی زاویه‌ای؟","Mass 2 kg and stiffness 200 N/m. Natural angular frequency?","۱۰ rad/s؛ √(200/2).","10 rad/s; √(200/2)."),
 q("فرکانس زاویه‌ای 20 rad/s؛ فرکانس بر حسب Hz؟","Angular frequency 20 rad/s. Frequency in Hz?","حدود ۳٫۱۸ Hz؛ 20/(2π).","About 3.18 Hz; 20/(2π)."),
 q("نسبت میرایی ζ=1 چه حالتی است؟","What is the damping condition at ζ=1?","میرایی بحرانی.","Critically damped."),
 q("در رزونانس دامنه پاسخ ایده‌آل بدون میرایی چه می‌شود؟","What happens to ideal undamped response amplitude at resonance?","از نظر نظری نامحدود می‌شود.","Theoretically becomes unbounded."),
 q("افزایش سختی فنر با جرم ثابت چه اثری بر فرکانس طبیعی دارد؟","Effect of increasing spring stiffness at fixed mass on natural frequency?","افزایش می‌یابد.","It increases."),
 q("میرایی ویسکوز با چه کمیتی متناسب است؟","Viscous damping force is proportional to what?","سرعت نسبی.","Relative velocity."),
 q("واحد سختی فنر چیست؟","Unit of spring stiffness?","نیوتن بر متر.","N/m."),
 q("ارتعاش اجباری با چه عاملی ایجاد می‌شود؟","What causes forced vibration?","نیروی تحریک خارجی متغیر با زمان.","A time-varying external excitation."),
 q("اگر جرم چهار برابر شود و k ثابت بماند، ωn چه می‌شود؟","If mass quadruples while k is fixed, what happens to ωn?","نصف می‌شود.","It halves."),
 q("نسبت میرایی کمتر از یک نشان‌دهنده چیست؟","What does damping ratio below one indicate?","سیستم کم‌میرای نوسانی.","An underdamped oscillatory system.")
 )),
 Lesson("math","ریاضی مهندسی","Engineering Mathematics","معادلات دیفرانسیل، لاپلاس و سری‌ها","Differential equations, Laplace and series",
 listOf(formula("مشتق توان","d(xⁿ)/dx = nxⁿ⁻¹","قاعده مشتق‌گیری تابع توانی.","Power rule."),formula("انتگرال توان","∫xⁿdx = xⁿ⁺¹/(n+1)+C","برای n≠−1.","For n not equal to −1."),formula("تبدیل لاپلاس","L{1} = 1/s","برای s مثبت.","For positive s."),formula("معادله دیفرانسیل مرتبه اول","dy/dx + Py = Q","فرم خطی استاندارد.","Standard linear form.")),
 listOf("معادله دیفرانسیل رابطه تابع مجهول و مشتق‌های آن است." to "A differential equation relates an unknown function to its derivatives.","شرط اولیه مقدار پاسخ را در یک زمان مشخص تعیین می‌کند." to "An initial condition specifies the solution at a given time.","تبدیل لاپلاس معادلات دیفرانسیل را به معادلات جبری تبدیل می‌کند." to "Laplace transforms convert differential equations into algebraic equations."),
 listOf(
 q("مشتق x³ چیست؟","Derivative of x³?","۳x².","3x²."),
 q("انتگرال 2x نسبت به x چیست؟","Integral of 2x with respect to x?","x²+C.","x²+C."),
 q("حل معادله dy/dx=0 چیست؟","General solution of dy/dx=0?","y=C؛ تابع ثابت.","y=C; a constant."),
 q("تبدیل لاپلاس تابع ثابت 5 چیست؟","Laplace transform of constant 5?","5/s.","5/s."),
 q("مشتق sin(x) چیست؟","Derivative of sin(x)?","cos(x).","cos(x)."),
 q("انتگرال cos(x) چیست؟","Integral of cos(x)?","sin(x)+C.","sin(x)+C."),
 q("ریشه‌های معادله x²−5x+6=0 چیست؟","Roots of x²−5x+6=0?","۲ و ۳.","2 and 3."),
 q("مقدار e⁰ چقدر است؟","What is e⁰?","۱.","1."),
 q("مشتق تابع ln(x) چیست؟","Derivative of ln(x)?","۱/x برای x مثبت.","1/x for x>0."),
 q("هدف شرط مرزی در معادله دیفرانسیل چیست؟","Purpose of a boundary condition in a differential equation?","تعیین پاسخ با اعمال مقادیر یا قیود در مرز دامنه.","Determine the solution using values or constraints at domain boundaries.")
 ))
).map { lesson -> lesson.copy(formulas = lesson.formulas + extraFormulas(lesson.id)) }

@Composable
fun LessonsPage(fa:Boolean,fg:Color,muted:Color,surface:Color) {
    var selected by remember { mutableStateOf<Lesson?>(null) }
    var section by remember { mutableStateOf(0) }
    var courseSearch by remember { mutableStateOf("") }
    BackHandler(enabled=selected!=null) { selected=null }
    CompositionLocalProvider(LocalLayoutDirection provides if(fa) LayoutDirection.Rtl else LayoutDirection.Ltr) {
        if(selected==null) {
            Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(12.dp)) {
                Text(if(fa) "دروس مهندسی مکانیک" else "Mechanical Engineering Courses",color=fg,fontSize=23.sp,fontWeight=FontWeight.Bold)
                Text(if(fa) "درس را انتخاب کن و مفاهیم، فرمول‌ها و تمرین‌ها را مرور کن." else "Choose a course to study concepts, formulas and practice.",color=muted,fontSize=12.sp)
                OutlinedTextField(value=courseSearch,onValueChange={courseSearch=it},modifier=Modifier.fillMaxWidth(),singleLine=true,placeholder={Text(if(fa)"جست‌وجوی درس..." else "Search courses...")},leadingIcon={Icon(Icons.Default.Search,null)})
                LazyColumn(verticalArrangement=Arrangement.spacedBy(9.dp),contentPadding=PaddingValues(bottom=16.dp)) {
                    items(lessons.filter { it.fa.contains(courseSearch,true) || it.en.contains(courseSearch,true) || it.descFa.contains(courseSearch,true) || it.descEn.contains(courseSearch,true) }) { lesson ->
                        Surface(color=surface,shape=RoundedCornerShape(16.dp),modifier=Modifier.fillMaxWidth().clickable{selected=lesson;section=0}) {
                            Row(Modifier.padding(15.dp),verticalAlignment=Alignment.CenterVertically) {
                                Surface(color=Color(0xFF173E5D),shape=RoundedCornerShape(12.dp)) { Icon(Icons.Default.MenuBook,null,tint=Color(0xFF59C2FF),modifier=Modifier.padding(11.dp).size(23.dp)) }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(if(fa) lesson.fa else lesson.en,color=fg,fontWeight=FontWeight.Bold)
                                    Text(if(fa) lesson.descFa else lesson.descEn,color=muted,fontSize=11.sp)
                                    Text(if(fa) "${lesson.questions.size} نمونه سؤال • ${lesson.formulas.size} فرمول" else "${lesson.questions.size} questions • ${lesson.formulas.size} formulas",color=Color(0xFF35AEEF),fontSize=10.sp)
                                }
                                Icon(Icons.Default.ChevronRight,null,tint=muted)
                            }
                        }
                    }
                }
            }
        } else {
            val lesson=selected!!
            Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment=Alignment.CenterVertically) {
                    IconButton(onClick={selected=null}) { Icon(Icons.Default.ArrowBack,null,tint=fg) }
                    Column(Modifier.weight(1f)) {
                        Text(if(fa) lesson.fa else lesson.en,color=fg,fontSize=20.sp,fontWeight=FontWeight.Bold)
                        Text(if(fa) lesson.descFa else lesson.descEn,color=muted,fontSize=11.sp)
                    }
                }
                Row(Modifier.fillMaxWidth().background(surface,RoundedCornerShape(13.dp)).padding(4.dp),horizontalArrangement=Arrangement.spacedBy(4.dp)) {
                    listOf(if(fa) "فرمول‌ها و مفاهیم" else "Formulas & Concepts",if(fa) "نمونه‌سؤال‌ها" else "Practice Questions").forEachIndexed { i,label ->
                        Surface(modifier=Modifier.weight(1f).clickable{section=i},color=if(section==i) Color(0xFF087FC2) else Color.Transparent,shape=RoundedCornerShape(10.dp)) {
                            Text(label,color=if(section==i) Color.White else muted,textAlign=TextAlign.Center,fontSize=12.sp,fontWeight=FontWeight.SemiBold,modifier=Modifier.padding(vertical=11.dp))
                        }
                    }
                }
                if(section==0) {
                    var formulaSearch by remember(lesson.id) { mutableStateOf("") }
                    LazyColumn(verticalArrangement=Arrangement.spacedBy(9.dp),contentPadding=PaddingValues(bottom=18.dp)) {
                        item { Text(if(fa) "فرمول‌های کلیدی" else "Key Formulas",color=fg,fontWeight=FontWeight.Bold,fontSize=16.sp) }
                        item { OutlinedTextField(value=formulaSearch,onValueChange={formulaSearch=it},modifier=Modifier.fillMaxWidth(),singleLine=true,placeholder={Text(if(fa)"جست‌وجوی فرمول یا نماد..." else "Search formula or symbol...")},leadingIcon={Icon(Icons.Default.Search,null)}) }
                        items(lesson.formulas.filter { it.titleFa.contains(formulaSearch,true) || it.titleEn.contains(formulaSearch,true) || it.equation.contains(formulaSearch,true) || it.noteFa.contains(formulaSearch,true) }) { f ->
                            Surface(color=surface,shape=RoundedCornerShape(14.dp),modifier=Modifier.fillMaxWidth()) {
                                Column(Modifier.fillMaxWidth().padding(14.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) {
                                    Text(if(fa) f.titleFa else f.titleEn,color=fg,fontWeight=FontWeight.Bold,fontSize=13.sp)
                                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) { Text(f.equation,color=Color(0xFF35B6FF),fontSize=19.sp,fontWeight=FontWeight.Bold,textAlign=TextAlign.Start) }
                                    Text(if(fa) f.noteFa else f.noteEn,color=muted,fontSize=11.sp)
                                }
                            }
                        }
                        item { Text(if(fa) "مفاهیم مهم" else "Important Concepts",color=fg,fontWeight=FontWeight.Bold,fontSize=16.sp,modifier=Modifier.padding(top=8.dp)) }
                        items(lesson.concepts) { c ->
                            Surface(color=surface,shape=RoundedCornerShape(13.dp)) {
                                Column(Modifier.fillMaxWidth().padding(13.dp)) {
                                    Text("• ${c.first}",color=fg,fontSize=12.sp)
                                    Text("• ${c.second}",color=muted,fontSize=11.sp,modifier=Modifier.padding(top=5.dp))
                                }
                            }
                        }
                    }
                } else {
                    LazyColumn(verticalArrangement=Arrangement.spacedBy(9.dp),contentPadding=PaddingValues(bottom=18.dp)) {
                        item { Text(if(fa) "۱۰ سؤال منتخب برای مرور و تمرین" else "10 selected review questions",color=muted,fontSize=11.sp) }
                        items(lesson.questions.withIndex().toList()) { indexed ->
                            var reveal by remember(lesson.id,indexed.index) { mutableStateOf(false) }
                            Surface(color=surface,shape=RoundedCornerShape(14.dp)) {
                                Column(Modifier.fillMaxWidth().padding(14.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                                    Text(if(fa) "سؤال ${indexed.index+1}" else "Question ${indexed.index+1}",color=Color(0xFF35B6FF),fontSize=10.sp,fontWeight=FontWeight.Bold)
                                    Text(if(fa) indexed.value.qFa else indexed.value.qEn,color=fg,fontSize=13.sp,fontWeight=FontWeight.Medium)
                                    TextButton(onClick={reveal=!reveal},contentPadding=PaddingValues(0.dp)) { Text(if(reveal) (if(fa) "پنهان‌کردن پاسخ" else "Hide answer") else (if(fa) "نمایش پاسخ تشریحی" else "Show solution")) }
                                    if(reveal) {
                                        HorizontalDivider(color=muted.copy(alpha=.2f))
                                        Text(if(fa) indexed.value.aFa else indexed.value.aEn,color=fg,fontSize=12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FormulaLibraryPage(fa:Boolean,fg:Color,muted:Color,surface:Color) {
    var search by remember { mutableStateOf("") }
    var selectedCourse by remember { mutableStateOf("all") }
    val allFormulas = lessons.flatMap { lesson -> lesson.formulas.map { lesson to it } }
    val filtered = allFormulas.filter { (lesson, item) ->
        (selectedCourse=="all" || lesson.id==selectedCourse) &&
        (search.isBlank() || item.titleFa.contains(search,true) || item.titleEn.contains(search,true) ||
            item.equation.contains(search,true) || item.noteFa.contains(search,true) ||
            item.noteEn.contains(search,true) || lesson.fa.contains(search,true) || lesson.en.contains(search,true))
    }
    CompositionLocalProvider(LocalLayoutDirection provides if(fa) LayoutDirection.Rtl else LayoutDirection.Ltr) {
        Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(10.dp)) {
            Text(if(fa)"کتابخانه فرمول‌های مهندسی" else "Engineering Formula Library",color=fg,fontSize=22.sp,fontWeight=FontWeight.Bold)
            Text(if(fa)"جست‌وجو در کل درس‌ها؛ فرمول‌ها همراه با توضیح و نام درس" else "Search across all courses, with explanations and course labels",color=muted,fontSize=11.sp)
            OutlinedTextField(value=search,onValueChange={search=it},modifier=Modifier.fillMaxWidth(),singleLine=true,placeholder={Text(if(fa)"نام فرمول، نماد یا درس..." else "Formula, symbol or course...")},leadingIcon={Icon(Icons.Default.Search,null)})
            Row(Modifier.fillMaxWidth().horizontalScroll(androidx.compose.foundation.rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                FilterChip(selected=selectedCourse=="all",onClick={selectedCourse="all"},label={Text(if(fa)"همه درس‌ها" else "All courses")})
                lessons.forEach { lesson ->
                    FilterChip(selected=selectedCourse==lesson.id,onClick={selectedCourse=lesson.id},label={Text(if(fa)lesson.fa else lesson.en)})
                }
            }
            Text(if(fa)"${filtered.size} فرمول" else "${filtered.size} formulas",color=muted,fontSize=10.sp)
            LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp),contentPadding=PaddingValues(bottom=18.dp)) {
                items(filtered) { (lesson,item) ->
                    Surface(color=surface,shape=RoundedCornerShape(14.dp)) {
                        Column(Modifier.fillMaxWidth().padding(13.dp),verticalArrangement=Arrangement.spacedBy(5.dp)) {
                            Text(if(fa)item.titleFa else item.titleEn,color=fg,fontWeight=FontWeight.Bold,fontSize=13.sp)
                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                                Text(item.equation,color=Color(0xFF35B6FF),fontSize=18.sp,fontWeight=FontWeight.Bold)
                            }
                            Text(if(fa)item.noteFa else item.noteEn,color=muted,fontSize=11.sp)
                            Text(if(fa)lesson.fa else lesson.en,color=Color(0xFF35AEEF),fontSize=10.sp,fontWeight=FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}
