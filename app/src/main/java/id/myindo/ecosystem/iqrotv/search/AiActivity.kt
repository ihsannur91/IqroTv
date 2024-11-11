package id.myindo.ecosystem.iqrotv.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import id.myindo.ecosystem.iqrotv.R
import id.myindo.ecosystem.iqrotv.adapters.ChatAdapter
import id.myindo.ecosystem.iqrotv.databinding.ActivityAiBinding
import java.util.Locale

class AiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAiBinding
    private lateinit var chatAdapter: ChatAdapter
    private val chatList = mutableListOf<String>()
    private lateinit var gestureDetector: GestureDetectorCompat
    private lateinit var inputField: TextInputEditText

    private val dummyData = mapOf(
        "ayat ayat tentang hari kiamat" to "Berikut ini ayat-ayat tentang hari kiamat dalam Al-Qur'an : \n" +
                "\n" +
                "1. Surah Al-Qiyamah (75:1-4)\n" +
                "Allah bersumpah atas kedatangan hari kiamat dan menyadarkan manusia akan kuasa-Nya yang mampu membangkitkan kembali setiap tulang belulang.\n" +
                "\n" +
                "Ayat 1\n" +
                "اَلَاۤ اُقْسِمُ بِيَوْمِ الْقِيٰمَةِ\n" +
                "\"Aku bersumpah demi hari kiamat,\"\n" +
                "\n" +
                "Ayat 2\n" +
                "وَلَاۤ اُقْسِمُ بِالنَّفْسِ اللَّوَّامَةِ\n" +
                "\"dan aku bersumpah dengan jiwa yang selalu menyesali (dirinya sendiri).\"\n" +
                "\n" +
                "Ayat 3\n" +
                "اَيَحْسَبُ الْاِنْسَانُ اَلَّنْ نَّجْمَعَ عِظَامَهٗۗ\n" +
                "\"Apakah manusia mengira bahwa Kami tidak akan mengumpulkan (kembali) tulang belulangnya?\"\n" +
                "\n" +
                "Ayat 4\n" +
                "بَلٰى قٰدِرِيْنَ عَلٰۤى اَنْ نُّسَوِّيَ بَنَانَهٗ\n" +
                "\"Bukan demikian, Kami mampu menyusun (kembali) jari-jemarinya dengan sempurna.\"\n" +
                "\n" +
                "2. Surah Al-Zalzalah (99:1-2)\n" +
                "Ayat ini menggambarkan kedahsyatan hari kiamat, ketika bumi bergoncang dahsyat dan mengeluarkan segala beban berat yang dikandungnya.\n" +
                "\n" +
                "Ayat 1\n" +
                "اِذَا زُلْزِلَتِ الْاَرْضُ زِلْزَالَهَا\n" +
                "\"Apabila bumi digoncangkan dengan goncangannya (yang dahsyat),\"\n" +
                "\n" +
                "Ayat 2\n" +
                "وَاَخْرَجَتِ الْاَرْضُ اَثْقَالَهَا\n" +
                "\"dan bumi telah mengeluarkan beban-beban berat (yang dikandung)nya,\"\n" +
                "\n" +
                "3. Surah Al-Haqqah (69:13-15)\n" +
                "Di sini, Al-Qur’an menjelaskan peristiwa kiamat dengan tiupan sangkakala yang menghancurkan langit dan bumi hingga seluruh alam semesta terguncang.\n" +
                "\n" +
                "Ayat 13\n" +
                "فَاِذَا نُفِخَ فِى الصُّوْرِ نَفْخَةٌ وَّاحِدَةٌ\n" +
                "\"Maka apabila sangkakala ditiup sekali tiup,\"\n" +
                "\n" +
                "Ayat 14\n" +
                "وَّحُمِلَتِ الْاَرْضُ وَالْجِبَالُ فَدُكَّتَا دَكَّةً وَّاحِدَةً\n" +
                "\"dan diangkatlah bumi dan gunung-gunung, lalu dihancurkan keduanya sekali hancur,\"\n" +
                "\n" +
                "Ayat 15\n" +
                "فَيَوْمَىٕذٍ وَّقَعَتِ الْوَاقِعَةُ\n" +
                "\"maka pada hari itu terjadilah kiamat.\"\n" +
                "\n" +
                "4. Surah At-Takwir (81:1-6)\n" +
                "Ayat-ayat ini menggambarkan berbagai tanda besar menjelang kiamat, dari matahari yang digulung hingga gunung yang berterbangan.\n" +
                "\n" +
                "Ayat 1\n" +
                "اِذَا الشَّمْسُ كُوِّرَتْ\n" +
                "\"Apabila matahari digulung (padam),\"\n" +
                "\n" +
                "Ayat 2\n" +
                "وَاِذَا النُّجُوْمُ انْكَدَرَتْ\n" +
                "\"dan apabila bintang-bintang berjatuhan,\"\n" +
                "\n" +
                "Ayat 3\n" +
                "وَاِذَا الْجِبَالُ سُيِّرَتْ\n" +
                "\"dan apabila gunung-gunung dihancurkan,\"\n" +
                "\n" +
                "Ayat 4\n" +
                "وَاِذَا الْعِشَارُ عُطِّلَتْ\n" +
                "\"dan apabila unta-unta yang bunting ditinggalkan (tidak terurus),\"\n" +
                "\n" +
                "Ayat 5\n" +
                "وَاِذَا الْوُحُوْشُ حُشِرَتْ\n" +
                "\"dan apabila binatang-binatang liar dikumpulkan,\"\n" +
                "\n" +
                "Ayat 6\n" +
                "وَاِذَا الْبِحَارُ سُجِّرَتْ\n" +
                "\"dan apabila lautan dijadikan meluap,\"\n" +
                "\n" +
                "Semoga ayat-ayat ini membantu dalam memahami peristiwa hari kiamat dengan lebih mendalam, menggugah kesadaran kita akan kekuasaan Allah yang mampu membangkitkan kembali manusia dan menghancurkan alam semesta.",
        "ayat ayat tentang sholat" to "Berikut beberapa ayat Al-Qur'an tentang sholat yang menekankan pentingnya sholat, tata caranya, serta peringatan bagi orang yang meninggalkannya:\n" +
                "\n" +
                "1. Surah Al-Baqarah (2:43)\n" +
                "Allah SWT memerintahkan umat Islam untuk mendirikan sholat dan menguatkan persaudaraan dengan memberi zakat.\n" +
                "\n" +
                "Ayat\n" +
                "وَأَقِيمُوا الصَّلَاةَ وَآتُوا الزَّكَاةَ وَارْكَعُوا مَعَ الرَّاكِعِينَ\n" +
                "\"Dan dirikanlah sholat, tunaikanlah zakat, dan rukuklah beserta orang-orang yang rukuk.\"\n" +
                "2. Surah Al-Ankabut (29:45)\n" +
                "Dalam ayat ini, Allah menjelaskan bahwa sholat dapat mencegah seseorang dari perbuatan keji dan munkar.\n" +
                "\n" +
                "Ayat\n" +
                "اتْلُ مَا أُوحِيَ إِلَيْكَ مِنَ الْكِتَابِ وَأَقِمِ الصَّلَاةَ ۖ إِنَّ الصَّلَاةَ تَنْهَىٰ عَنِ الْفَحْشَاءِ وَالْمُنكَرِ ۗ وَلَذِكْرُ اللَّهِ أَكْبَرُ ۗ وَاللَّهُ يَعْلَمُ مَا تَصْنَعُونَ\n" +
                "\"Bacalah Kitab (Al-Qur'an) yang telah diwahyukan kepadamu dan dirikanlah sholat. Sesungguhnya sholat itu mencegah dari (perbuatan-perbuatan) keji dan mungkar. Dan sesungguhnya mengingat Allah (sholat) adalah lebih besar (keutamaannya). Allah mengetahui apa yang kamu kerjakan.\"\n" +
                "3. Surah Taha (20:14)\n" +
                "Allah memerintahkan Nabi Musa untuk mendirikan sholat sebagai bentuk penghambaan kepada-Nya.\n" +
                "\n" +
                "Ayat\n" +
                "إِنَّنِي أَنَا اللَّهُ لَا إِلَٰهَ إِلَّا أَنَا فَاعْبُدْنِي وَأَقِمِ الصَّلَاةَ لِذِكْرِي\n" +
                "\"Sesungguhnya Aku ini adalah Allah, tidak ada Tuhan selain Aku, maka sembahlah Aku dan dirikanlah sholat untuk mengingat-Ku.\"\n" +
                "4. Surah Al-Ma’un (107:4-5)\n" +
                "Allah memperingatkan orang yang lalai terhadap sholatnya, yakni mereka yang sholat hanya untuk pamer atau tidak mengerjakannya dengan sungguh-sungguh.\n" +
                "\n" +
                "Ayat 4\n" +
                "فَوَيْلٌ لِلْمُصَلِّينَ\n" +
                "\"Maka kecelakaanlah bagi orang-orang yang sholat,\"\n" +
                "\n" +
                "Ayat 5\n" +
                "الَّذِينَ هُمْ عَنْ صَلَاتِهِمْ سَاهُونَ\n" +
                "\"yaitu orang-orang yang lalai terhadap sholatnya.\"\n" +
                "\n" +
                "5. Surah Al-Muddathir (74:42-43)\n" +
                "Allah menggambarkan keadaan orang-orang yang di akhirat nanti berada di neraka, dan salah satu sebabnya adalah karena mereka tidak mengerjakan sholat.\n" +
                "\n" +
                "Ayat 42\n" +
                "مَا سَلَكَكُمْ فِي سَقَرَ\n" +
                "\"Apakah yang memasukkan kamu ke dalam (neraka) Saqar?\"\n" +
                "\n" +
                "Ayat 43\n" +
                "قَالُوا لَمْ نَكُ مِنَ الْمُصَلِّينَ\n" +
                "\"Mereka menjawab, 'Dahulu kami tidak termasuk orang-orang yang mengerjakan sholat.'\"\n" +
                "\n" +
                "6. Surah Al-Baqarah (2:238)\n" +
                "Ayat ini mengingatkan umat Islam untuk menjaga sholat dengan sungguh-sungguh, terutama sholat yang paling utama, yaitu sholat tengah (Ashar).\n" +
                "\n" +
                "Ayat\n" +
                "حَافِظُوا عَلَى الصَّلَوَاتِ وَالصَّلَاةِ الْوُسْطَىٰ وَقُومُوا لِلَّهِ قَانِتِينَ\n" +
                "\"Peliharalah semua sholatmu, dan (peliharalah) sholat wustha. Berdirilah untuk Allah (dalam sholatmu) dengan khusyuk.\" Semoga ayat-ayat ini menginspirasi dan memberikan pemahaman yang lebih dalam tentang pentingnya mendirikan sholat dalam kehidupan kita sebagai umat Islam. Setiap ayat menunjukkan aspek yang berbeda dari sholat, mulai dari perintah, hikmah, hingga peringatan bagi yang lalai.\n" +
                "\n" +
                "Dengan mendirikan sholat secara rutin dan khusyuk, kita diingatkan untuk senantiasa dekat dengan Allah, menjaga diri dari perbuatan keji, dan senantiasa memohon petunjuk serta kekuatan-Nya. Ayat-ayat tersebut mengajak kita untuk tidak hanya memahami kewajiban sholat, tetapi juga menjalankan dengan ikhlas dan konsisten, sebagai bukti cinta dan kepatuhan kita kepada Sang Pencipta.\n" +
                "\n" +
                "Semoga kita senantiasa mampu menjaga sholat dalam kehidupan kita sehari-hari dan dapat mengambil pelajaran dari makna mendalam setiap ayat yang Allah SWT turunkan.",
        "ayat ayat tentang zakat" to "Berikut ini adalah beberapa ayat Al-Qur'an yang menjelaskan tentang zakat, perintahnya, dan tujuan dari pelaksanaan zakat dalam Islam:\n" +
                "\n" +
                "1. Surah Al-Baqarah (2:43)\n" +
                "Allah SWT memerintahkan umat Islam untuk mendirikan sholat dan menunaikan zakat sebagai bentuk ketaatan dan kepedulian kepada sesama.\n" +
                "\n" +
                "Ayat\n" +
                "وَأَقِيمُوا الصَّلَاةَ وَآتُوا الزَّكَاةَ وَارْكَعُوا مَعَ الرَّاكِعِينَ\n" +
                "\"Dan dirikanlah sholat, tunaikanlah zakat, dan rukuklah beserta orang-orang yang rukuk.\"\n" +
                "2. Surah At-Taubah (9:60)\n" +
                "Ayat ini menjelaskan kategori orang-orang yang berhak menerima zakat, yaitu delapan golongan yang diatur dalam Islam.\n" +
                "\n" +
                "Ayat\n" +
                "إِنَّمَا الصَّدَقَاتُ لِلْفُقَرَاءِ وَالْمَسَاكِينِ وَالْعَامِلِينَ عَلَيْهَا وَالْمُؤَلَّفَةِ قُلُوبُهُمْ وَفِي الرِّقَابِ وَالْغَارِمِينَ وَفِي سَبِيلِ اللَّهِ وَابْنِ السَّبِيلِ فَرِيضَةً مِّنَ اللَّهِ ۗ وَاللَّهُ عَلِيمٌ حَكِيمٌ\n" +
                "\"Sesungguhnya zakat-zakat itu hanyalah untuk orang-orang fakir, orang-orang miskin, pengurus-pengurus zakat, para mu'allaf yang dibujuk hatinya, untuk (memerdekakan) hamba sahaya, orang-orang yang berhutang, untuk jalan Allah dan untuk mereka yang sedang dalam perjalanan, sebagai suatu ketetapan yang diwajibkan Allah. Allah Maha Mengetahui lagi Maha Bijaksana.\"\n" +
                "3. Surah Al-Baqarah (2:177)\n" +
                "Ayat ini mengajarkan tentang kebajikan yang sempurna, termasuk di antaranya adalah memberi zakat kepada yang membutuhkan sebagai bentuk keimanan dan kebaikan hati.\n" +
                "\n" +
                "Ayat\n" +
                "لَيْسَ الْبِرَّ أَنْ تُوَلُّوا وُجُوهَكُمْ قِبَلَ الْمَشْرِقِ وَالْمَغْرِبِ وَلَٰكِنَّ الْبِرَّ مَنْ آمَنَ بِاللَّهِ وَالْيَوْمِ الْآخِرِ وَالْمَلَائِكَةِ وَالْكِتَابِ وَالنَّبِيِّينَ وَآتَى الْمَالَ عَلَىٰ حُبِّهِ ذَوِي الْقُرْبَىٰ وَالْيَتَامَىٰ وَالْمَسَاكِينَ وَابْنَ السَّبِيلِ وَالسَّائِلِينَ وَفِي الرِّقَابِ وَأَقَامَ الصَّلَاةَ وَآتَى الزَّكَاةَ وَالْمُوفُونَ بِعَهْدِهِمْ إِذَا عَاهَدُوا وَالصَّابِرِينَ فِي الْبَأْسَاءِ وَالضَّرَّاءِ وَحِينَ الْبَأْسِ ۗ أُولَٰئِكَ الَّذِينَ صَدَقُوا وَأُولَٰئِكَ هُمُ الْمُتَّقُونَ\n" +
                "\"Bukanlah menghadapkan wajahmu ke arah timur dan barat itu suatu kebajikan, tetapi sesungguhnya kebajikan itu adalah beriman kepada Allah, hari kemudian, malaikat-malaikat, kitab-kitab, dan nabi-nabi serta memberikan harta yang dicintainya kepada kerabat, anak-anak yatim, orang-orang miskin, musafir (yang membutuhkan), dan orang-orang yang meminta-minta; dan (memerdekakan) hamba sahaya, mendirikan sholat, dan menunaikan zakat; dan orang-orang yang menepati janjinya apabila ia berjanji, dan orang-orang yang sabar dalam kesempitan, penderitaan, dan dalam peperangan. Mereka itulah orang-orang yang benar (imannya); dan mereka itulah orang-orang yang bertakwa.\"\n" +
                "4. Surah Al-Mu'minun (23:1-4)\n" +
                "Ayat ini menggambarkan ciri-ciri orang beriman yang sukses, yaitu mereka yang menjaga sholat dan menunaikan zakat.\n" +
                "\n" +
                "Ayat 1-4\n" +
                "قَدْ أَفْلَحَ الْمُؤْمِنُونَ\n" +
                "\"Sesungguhnya beruntunglah orang-orang yang beriman,\"\n" +
                "\n" +
                "الَّذِينَ هُمْ فِي صَلَاتِهِمْ خَاشِعُونَ\n" +
                "\"(yaitu) orang-orang yang khusyuk dalam sholatnya,\"\n" +
                "\n" +
                "وَالَّذِينَ هُمْ عَنِ اللَّغْوِ مُعْرِضُونَ\n" +
                "\"dan orang-orang yang menjauhkan diri dari (perbuatan dan perkataan) yang tiada berguna,\"\n" +
                "\n" +
                "وَالَّذِينَ هُمْ لِلزَّكَاةِ فَاعِلُونَ\n" +
                "\"dan orang-orang yang menunaikan zakat.\"\n" +
                "\n" +
                "5. Surah Al-Lail (92:18-21)\n" +
                "Ayat ini menjelaskan bahwa orang yang bertakwa dan menyucikan hartanya dengan berzakat akan mendapatkan balasan yang terbaik dari Allah.\n" +
                "\n" +
                "Ayat 18-21\n" +
                "الَّذِي يُؤْتِي مَالَهُ يَتَزَكَّىٰ\n" +
                "\"(yaitu) orang yang memberikan hartanya (di jalan Allah) untuk membersihkannya (dengan zakat),\"\n" +
                "\n" +
                "وَمَا لِأَحَدٍ عِندَهُ مِن نِّعْمَةٍ تُجْزَىٰ\n" +
                "\"padahal tidak ada seseorangpun memberikan suatu nikmat kepadanya yang harus dibalasnya,\"\n" +
                "\n" +
                "إِلَّا ابْتِغَاءَ وَجْهِ رَبِّهِ الْأَعْلَىٰ\n" +
                "\"tetapi (dia memberikan itu semata-mata) karena mencari keridaan Tuhannya Yang Mahatinggi.\"\n" +
                "\n" +
                "وَلَسَوْفَ يَرْضَىٰ\n" +
                "\"Dan kelak dia benar-benar mendapat kepuasan.\"\n" +
                "\n" +
                "6. Surah At-Taubah (9:103)\n" +
                "Ayat ini menggambarkan bahwa zakat berfungsi untuk menyucikan dan membersihkan jiwa serta harta seorang muslim.\n" +
                "\n" +
                "Ayat\n" +
                "خُذْ مِنْ أَمْوَالِهِمْ صَدَقَةً تُطَهِّرُهُمْ وَتُزَكِّيهِمْ بِهَا وَصَلِّ عَلَيْهِمْ ۖ إِنَّ صَلَاتَكَ سَكَنٌ لَّهُمْ ۗ وَاللَّهُ سَمِيعٌ عَلِيمٌ\n" +
                "\"Ambillah zakat dari sebagian harta mereka, dengan zakat itu kamu membersihkan dan menyucikan mereka, dan mendoalah untuk mereka. Sesungguhnya doa kamu itu (menjadi) ketenteraman jiwa bagi mereka. Dan Allah Maha Mendengar, Maha Mengetahui.\"\n" +
                "Semoga ayat-ayat ini menjadi pengingat bagi kita tentang kewajiban zakat dan hikmah yang besar dari melaksanakannya.",
        "ayat ayat tentang puasa" to "Berikut adalah beberapa ayat Al-Qur'an yang membahas tentang puasa, khususnya puasa di bulan Ramadan, yang merupakan salah satu rukun Islam dan memiliki banyak hikmah serta keutamaan:\n" +
                "\n" +
                "1. Surah Al-Baqarah (2:183)\n" +
                "Ayat ini adalah perintah Allah untuk berpuasa bagi orang-orang yang beriman, menunjukkan bahwa puasa telah diwajibkan juga pada umat-umat sebelumnya sebagai sarana untuk meningkatkan ketakwaan.\n" +
                "\n" +
                "Ayat\n" +
                "يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِن قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ\n" +
                "\"Wahai orang-orang yang beriman, diwajibkan atas kamu berpuasa sebagaimana diwajibkan atas orang-orang sebelum kamu agar kamu bertakwa.\"\n" +
                "2. Surah Al-Baqarah (2:184)\n" +
                "Ayat ini menjelaskan bahwa puasa dilaksanakan dalam hari-hari tertentu dan memberikan kemudahan bagi mereka yang sakit atau dalam perjalanan, dengan mengganti puasa di hari lain atau dengan membayar fidyah.\n" +
                "\n" +
                "Ayat\n" +
                "أَيَّامًا مَعْدُودَاتٍ ۚ فَمَن كَانَ مِنكُم مَّرِيضًا أَوْ عَلَىٰ سَفَرٍ فَعِدَّةٌ مِّنْ أَيَّامٍ أُخَرَ ۚ وَعَلَى الَّذِينَ يُطِيقُونَهُ فِدْيَةٌ طَعَامُ مِسْكِينٍ ۖ فَمَن تَطَوَّعَ خَيْرًا فَهُوَ خَيْرٌ لَّهُ ۚ وَأَن تَصُومُوا خَيْرٌ لَّكُمْ إِن كُنتُمْ تَعْلَمُونَ\n" +
                "\"(Yaitu) dalam beberapa hari tertentu. Maka barangsiapa di antara kamu ada yang sakit atau dalam perjalanan (lalu ia berbuka), maka (wajiblah baginya berpuasa) sebanyak hari yang ditinggalkan itu pada hari-hari yang lain. Dan wajib bagi orang-orang yang berat menjalankannya (jika mereka tidak berpuasa) membayar fidyah, (yaitu): memberi makan seorang miskin. Barangsiapa dengan kerelaan hati mengerjakan kebajikan, maka itulah yang lebih baik baginya. Dan berpuasa lebih baik bagimu jika kamu mengetahui.\"\n" +
                "3. Surah Al-Baqarah (2:185)\n" +
                "Ayat ini menjelaskan bahwa bulan Ramadan adalah bulan di mana Al-Qur'an diturunkan sebagai petunjuk bagi manusia, dan dalam ayat ini pula Allah menegaskan bahwa berpuasa di bulan Ramadan adalah kewajiban bagi orang-orang beriman, kecuali bagi yang dalam kondisi khusus.\n" +
                "\n" +
                "Ayat\n" +
                "شَهْرُ رَمَضَانَ الَّذِي أُنزِلَ فِيهِ الْقُرْآنُ هُدًى لِّلنَّاسِ وَبَيِّنَاتٍ مِّنَ الْهُدَىٰ وَالْفُرْقَانِ ۚ فَمَن شَهِدَ مِنكُمُ الشَّهْرَ فَلْيَصُمْهُ ۖ وَمَن كَانَ مَرِيضًا أَوْ عَلَىٰ سَفَرٍ فَعِدَّةٌ مِّنْ أَيَّامٍ أُخَرَ ۗ يُرِيدُ اللَّهُ بِكُمُ الْيُسْرَ وَلَا يُرِيدُ بِكُمُ الْعُسْرَ وَلِتُكْمِلُوا الْعِدَّةَ وَلِتُكَبِّرُوا اللَّهَ عَلَىٰ مَا هَدَاكُمْ وَلَعَلَّكُمْ تَشْكُرُونَ\n" +
                "\"Bulan Ramadan adalah (bulan) yang di dalamnya diturunkan Al-Qur'an, sebagai petunjuk bagi manusia dan penjelasan-penjelasan mengenai petunjuk itu serta pembeda (antara yang benar dan yang batil). Karena itu, barangsiapa di antara kamu ada di bulan itu, maka berpuasalah. Dan barangsiapa sakit atau dalam perjalanan (lalu ia berbuka), maka (wajib menggantinya), sebanyak hari yang ditinggalkannya itu, pada hari-hari yang lain. Allah menghendaki kemudahan bagimu dan tidak menghendaki kesulitan bagimu. Dan hendaklah kamu mencukupkan bilangannya dan hendaklah kamu mengagungkan Allah atas petunjuk-Nya yang diberikan kepadamu, supaya kamu bersyukur.\"\n" +
                "4. Surah Al-Baqarah (2:187)\n" +
                "Ayat ini menjelaskan aturan mengenai waktu-waktu puasa, yaitu dari terbit fajar hingga terbenam matahari, serta memberikan kebebasan bagi pasangan suami-istri untuk bersama di malam hari saat berpuasa.\n" +
                "\n" +
                "Ayat\n" +
                "أُحِلَّ لَكُمْ لَيْلَةَ الصِّيَامِ الرَّفَثُ إِلَىٰ نِسَائِكُمْ ۚ هُنَّ لِبَاسٌ لَّكُمْ وَأَنتُمْ لِبَاسٌ لَّهُنَّ ۗ عَلِمَ اللَّهُ أَنَّكُمْ كُنتُمْ تَخْتَانُونَ أَنفُسَكُمْ فَتَابَ عَلَيْكُمْ وَعَفَا عَنكُمْ فَالْآنَ بَاشِرُوهُنَّ وَابْتَغُوا مَا كَتَبَ اللَّهُ لَكُمْ ۚ وَكُلُوا وَاشْرَبُوا حَتَّىٰ يَتَبَيَّنَ لَكُمُ الْخَيْطُ الْأَبْيَضُ مِنَ الْخَيْطِ الْأَسْوَدِ مِنَ الْفَجْرِ ۖ ثُمَّ أَتِمُّوا الصِّيَامَ إِلَى اللَّيْلِ ۚ وَلَا تُبَاشِرُوهُنَّ وَأَنتُمْ عَاكِفُونَ فِي الْمَسَاجِدِ ۗ تِلْكَ حُدُودُ اللَّهِ فَلَا تَقْرَبُوهَا ۗ كَذَٰلِكَ يُبَيِّنُ اللَّهُ آيَاتِهِ لِلنَّاسِ لَعَلَّهُمْ يَتَّقُونَ\n" +
                "\"Dihalalkan bagimu pada malam hari puasa bercampur dengan istri-istri kamu; mereka adalah pakaian bagimu, dan kamupun adalah pakaian bagi mereka. Allah mengetahui bahwa kamu tidak dapat menahan nafsumu, karena itu Allah mengampuni kamu dan memberi maaf kepadamu. Maka sekarang campurilah mereka dan carilah apa yang telah ditetapkan Allah bagimu. Makan dan minumlah hingga terang bagimu benang putih dari benang hitam, yaitu fajar. Kemudian sempurnakanlah puasa itu sampai (datang) malam. Tetapi janganlah kamu campuri mereka itu, sedang kamu beri'tikaf dalam mesjid. Itulah larangan Allah, maka janganlah kamu mendekatinya. Demikianlah Allah menerangkan ayat-ayat-Nya kepada manusia, supaya mereka bertakwa.\"\n" +
                "5. Surah Al-Ahzab (33:35)\n" +
                "Ayat ini memuji orang-orang yang sabar, taat, dermawan, dan yang rajin berpuasa, menunjukkan betapa pentingnya kualitas-kualitas ini dalam mencapai kebahagiaan di sisi Allah.\n" +
                "\n" +
                "Ayat\n" +
                "إِنَّ الْمُسْلِمِينَ وَالْمُسْلِمَاتِ وَالْمُؤْمِنِينَ وَالْمُؤْمِنَاتِ وَالْقَانِتِينَ وَالْقَانِتَاتِ وَالصَّادِقِينَ وَالصَّادِقَاتِ وَالصَّابِرِينَ وَالصَّابِرَاتِ وَالْخَاشِعِينَ وَالْخَاشِعَاتِ وَالْمُتَصَدِّقِينَ وَالْمُتَصَدِّقَاتِ وَالصَّائِمِينَ وَالصَّائِمَاتِ وَالْحَافِظِينَ فُرُوجَهُمْ وَالْحَافِظَاتِ وَالذَّاكِرِينَ اللَّهَ كَثِيرًا وَالذَّاكِرَاتِ أَعَدَّ اللَّهُ لَهُم مَّغْفِرَةً وَأَجْرًا عَظِيمًا\n" +
                "\"Sesungguhnya laki-laki dan perempuan yang muslim, laki-laki dan perempuan yang mukmin, laki-laki dan perempuan yang tetap dalam ketaatannya, laki-laki dan perempuan yang benar, laki-laki dan perempuan yang sabar, laki-laki dan perempuan yang khusyuk, laki-laki dan perempuan yang bersedekah, laki-laki dan perempuan yang berpuasa, laki-laki dan perempuan yang memelihara kehormatannya, laki-laki dan perempuan yang banyak menyebut (nama) Allah, Allah telah menyediakan untuk mereka ampunan dan pahala yang besar.\"\n" +
                "Ayat-ayat ini menegaskan bahwa puasa adalah ibadah yang membawa ketakwaan, pengampunan, serta pahala yang besar di sisi Allah."

    )
    private val typingDelay = 10L  // Delay antara karakter saat "mengetik"
    private lateinit var speechRecognizer: SpeechRecognizer
    private val RECORD_AUDIO_REQUEST_CODE = 1
    private lateinit var recognitionDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputField = findViewById<TextInputEditText>(R.id.search_input)
        val searchButton = findViewById<ConstraintLayout>(R.id.enter_button)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val placeholderLayout = findViewById<LinearLayout>(R.id.placeholderLayout)
        val micButton = findViewById<ConstraintLayout>(R.id.micButton)

        chatAdapter = ChatAdapter(chatList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = chatAdapter

        binding.imgBack.setOnClickListener {
            finish()
        }

        // Cek apakah ada input, jika tidak tampilkan placeholder
        searchButton.setOnClickListener {
            val input = inputField.text.toString().lowercase(Locale.getDefault())

            // Sembunyikan placeholder jika user sudah mengisi input
            if (input.isNotEmpty()) {
                placeholderLayout.visibility = View.GONE
                val response = dummyData[input] ?: "Maaf, saya tidak menemukan jawaban untuk pertanyaan Anda."

                // Tambahkan input pengguna ke dalam list
                chatList.add("Anda: $input")
                chatAdapter.notifyDataSetChanged()

                // Tampilkan animasi mengetik untuk jawaban
                simulateTypingResponse(response, recyclerView)

                // Kosongkan input field setelah submit
                inputField.text?.clear()
            }
        }

        gestureDetector = GestureDetectorCompat(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                closeKeyboard()
                return true
            }
        })

        // Set listener pada root view untuk mendeteksi gestur ketukan
        val rootView: View = findViewById(R.id.rootView)
        rootView.setOnClickListener {
         closeKeyboard()
        }
        binding.micButton.setOnClickListener {
            btnClick()
        }

    }

    private fun btnClick(){
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_RESULTS,Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Bertanyalah...")
        activityResultLauncher.launch(intent)
    }

    private val activityResultLauncher : ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if (result.resultCode == RESULT_OK && result.data != null){
                val resultData = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val spokenText = resultData?.get(0)
                binding.searchInput.text = spokenText?.let { Editable.Factory.getInstance().newEditable(it) }
            }
        }

    private fun simulateTypingResponse(response: String, recyclerView: RecyclerView) {
        var currentText = ""
        val handler = Handler(Looper.getMainLooper())

        // Dapatkan layout manager untuk memeriksa posisi scroll
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager

        for (i in response.indices) {
            handler.postDelayed({
                currentText += response[i]
                // Perbarui item terakhir dengan efek mengetik
                if (chatList.size > 0) {
                    chatList[chatList.size - 1] = "Menurut Pak Ustad Michael : $currentText"
                    chatAdapter.notifyItemChanged(chatList.size - 1)
                }

                // Cek jika pengguna berada di item paling akhir atau tidak
//                val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()
//                if (lastVisiblePosition == chatList.size - 2) {
//                    // Scroll ke bawah untuk memastikan teks terbaru terlihat jika di item paling akhir
//                    recyclerView.scrollToPosition(chatList.size - 1)
//                }
            }, i * typingDelay)
        }
    }



    private fun closeKeyboard() {
        // Tutup keyboard
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(inputField.windowToken, 0)
    }
}