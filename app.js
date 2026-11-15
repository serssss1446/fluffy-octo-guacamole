const keys=["power","vol_up","vol_down","ch_up","ch_down","mute"];
const statusEl=document.getElementById("irStatus");
function toast(msg){const t=document.getElementById("toast");t.textContent=msg;t.classList.add("show");setTimeout(()=>t.classList.remove("show"),2600);}
function bridgeAvailable(){return typeof window.DolphyIR!=="undefined";}
function checkIR(){
  if(!bridgeAvailable()){statusEl.textContent="افتح التطبيق على أندرويد لفحص مرسل IR.";return;}
  try{statusEl.textContent=DolphyIR.isAvailable()?"مرسل IR متاح ✓":"لم يتم اكتشاف مرسل IR في الهاتف";}
  catch(e){statusEl.textContent="تعذر فحص مرسل IR";}
}
function loadCodes(){keys.forEach(k=>{const v=localStorage.getItem("code_"+k);if(v)document.getElementById(k).value=v;});}
function saveCodes(){keys.forEach(k=>localStorage.setItem("code_"+k,document.getElementById(k).value.trim().toUpperCase()));toast("تم حفظ الأكواد على هذا الجهاز");}
function send(key){
  const input=document.getElementById(key);
  const code=(input.value||"").trim().replace(/^0x/i,"").toUpperCase();
  if(!/^[0-9A-F]{8}$/.test(code)){toast("أدخل كود NEC صحيحاً من 8 خانات لهذا الزر");return;}
  if(!bridgeAvailable()){toast("إرسال IR متاح من نسخة Android APK فقط");return;}
  try{
    const result=DolphyIR.sendNec(code);
    if(result==="OK")toast("تم إرسال إشارة IR");
    else if(result==="NO_IR")toast("هذا الهاتف لا يوفّر مرسل IR متاحاً للنظام");
    else if(result==="BAD_CODE")toast("الكود غير صحيح");
    else toast("تعذر إرسال الإشارة");
  }catch(e){toast("حدث خطأ أثناء إرسال IR");}
}
checkIR();loadCodes();
