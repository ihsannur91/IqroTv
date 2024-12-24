package id.myindo.ecosystem.iqrotv.alquran

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import id.myindo.ecosystem.iqrotv.R
import id.myindo.ecosystem.iqrotv.adapters.AyatPagerAdapter
import id.myindo.ecosystem.iqrotv.adapters.ImageAlphabetAdapter
import id.myindo.ecosystem.iqrotv.adapters.SurahAdapter
import id.myindo.ecosystem.iqrotv.data.Ayat
import id.myindo.ecosystem.iqrotv.data.Surah
import id.myindo.ecosystem.iqrotv.databinding.ActivityAlquranBinding

class AlquranActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAlquranBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlquranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val surahList = getSurahList()

        binding.imgBack.setOnClickListener {
            finish()
        }

        // Gambar dengan ukuran small, medium, large
        val imageList = listOf(
            Pair(R.drawable.img_alphabet, "small"),
            Pair(R.drawable.img_alphabet, "medium"),
            Pair(R.drawable.img_alphabet, "large")
        )

        // Default gambar terpilih
        var selectedSize = "small"

        // Set RecyclerView untuk gambar
        val adapter = ImageAlphabetAdapter(this,imageList) { size ->
            selectedSize = size
//            updateViewPagerFragment(size)
        }
        binding.imageRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.imageRecyclerView.adapter = adapter


        // Setup RecyclerView
        val surahRecyclerView: RecyclerView = findViewById(R.id.surah_recycler_view)
        surahRecyclerView.layoutManager = LinearLayoutManager(this)
        surahRecyclerView.adapter = SurahAdapter(surahList) { surah ->
            // Update ViewPager ketika surah dipilih
            val viewPager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager.adapter = AyatPagerAdapter(this, surah)

            binding.tvSurah.text = surah.name
            binding.tvMeaningSurah.text = surah.desc
            binding.tvSurahNumber.text = surah.ayatCount
        }

        // Setup ViewPager2 (default ke surah pertama)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = AyatPagerAdapter(this, surahList[0])
        viewPager.isUserInputEnabled = false

        // Update tampilan pertama dengan surah pertama
        binding.tvSurah.text = surahList[0].name
        binding.tvMeaningSurah.text = surahList[0].desc
        binding.tvSurahNumber.text = surahList[0].ayatCount

    }

//    private fun setupViewPager(defaultSize: String) {
//        val pagerAdapter = ViewPagerAdapter(this, defaultSize)
//        binding.viewPager.adapter = pagerAdapter
//    }
//
//    private fun updateViewPagerFragment(selectedSize: String) {
//        (binding.viewPager.adapter as? ViewPagerAdapter)?.updateSize(selectedSize)
//    }

    private fun getSurahList(): List<Surah> {
        return listOf(
            Surah(
                "Al-Alaq", "١","Segumpal Darah", listOf(
                    Ayat("١", "اِقۡرَاۡ بِاسۡمِ رَبِّكَ الَّذِىۡ خَلَقَ", "iqra' bismi rabbikalladzî khalaq", "Bacalah dengan (menyebut) nama Tuhanmu yang menciptakan!","alalaq_ayat1"),
                    Ayat("٢", "خَلَقَ الۡاِنۡسَانَ مِنۡ عَلَقٍ", "khalaqal-insâna min ‘alaq", "Dia menciptakan manusia dari segumpal darah.","alalaq_ayat2"),
                    Ayat("٣", "اِقۡرَاۡ وَرَبُّكَ الۡاَكۡرَمُۙ", "iqra' wa rabbukal-akram", "Bacalah! Tuhanmulah Yang Mahamulia","alalaq_ayat3"),
                    Ayat("٤", "الَّذِىۡ عَلَّمَ بِالۡقَلَمِۙ", "alladzî ‘allama bil-qalam", "yang mengajar (manusia) dengan pena.","alalaq_ayat4"),
                    Ayat("٥", "عَلَّمَ الۡاِنۡسَانَ مَا لَمۡ يَعۡلَمۡؕ", "‘allamal-insâna mâ lam ya‘lam", "Dia mengajarkan manusia apa yang tidak diketahuinya.","alalaq_ayat5"),
                    Ayat("٦", "كَلَّاۤ اِنَّ الۡاِنۡسَانَ لَيَطۡغٰٓىۙ", "kallâ innal-insâna layathghâ", "Sekali-kali tidak! Sesungguhnya manusia itu benar-benar melampaui batas","alalaq_ayat6"),
                    Ayat("٧", "اَنۡ رَّاٰه  ُ اسۡتَغۡنٰىؕ", "ar ra'âhustaghnâ", "ketika melihat dirinya serba berkecukupan.","alalaq_ayat7"),
                    Ayat("٨", "اِنَّ اِلٰى رَبِّكَ الرُّجۡعٰىؕ", "inna ilâ rabbikar-ruj‘â", "Sesungguhnya hanya kepada Tuhanmulah tempat kembali(-mu).","alalaq_ayat8"),
                    Ayat("٩", "اَرَءَيۡتَ الَّذِىۡ يَنۡهٰىؕ", "a ra'aitalladzî yan-hâ", "Tahukah kamu tentang orang yang melarang","alalaq_ayat9"),
                    Ayat("١٠", "عَبۡدًا اِذَا صَلّٰىؕ", "‘abdan idzâ shallâ", "seorang hamba ketika dia melaksanakan salat?","alalaq_ayat10"),
                    Ayat("١١", "اَرَءَيۡتَ اِنۡ كَانَ عَلَى الۡهُدٰٓىۙ", "a ra'aita ing kâna ‘alal-hudâ", "Bagaimana pendapatmu kalau terbukti dia berada di dalam kebenaran","alalaq_ayat11"),
                    Ayat("١٢", "اَوۡ اَمَرَ بِالتَّقۡوٰىۙ", "au amara bit-taqwâ", "atau dia menyuruh bertakwa (kepada Allah)?","alalaq_ayat12"),
                    Ayat("١٣", "اَرَءَيۡتَ اِنۡ كَذَّبَ وَتَوَلّٰىؕ", "a ra'aita ing kadzdzaba wa tawallâ", "Bagaimana pendapatmu kalau dia mendustakan (kebenaran) dan berpaling (dari keimanan)?","alalaq_ayat13"),
                    Ayat("١٤", "اَلَمۡ يَعۡلَمۡ بِاَنَّ اللّٰهَ يَرٰىؕ", "a lam ya‘lam bi'annallâha yarâ", "Tidakkah dia mengetahui bahwa sesungguhnya Allah melihat (segala perbuatannya)?","alalaq_ayat14"),
                    Ayat("١٥", "كَلَّا لَٮِٕنۡ لَّمۡ يَنۡتَهِ ۙ لَنَسۡفَعًۢا بِالنَّاصِيَةِۙ", "kallâ la'il lam yantahi lanasfa‘am bin-nâshiyah", "Sekali-kali tidak! Sungguh, jika dia tidak berhenti (berbuat demikian), niscaya Kami tarik ubun-ubunnya (ke dalam neraka),","alalaq_ayat15"),
                    Ayat("١٦", "نَاصِيَةٍ كَاذِبَةٍ خَاطِئَةٍُ", "nâshiyating kâdzibatin khâthi'ah", "(yaitu) ubun-ubun orang yang mendustakan (kebenaran) dan durhaka.","alalaq_ayat16"),
                    Ayat("١٧", "فَلۡيَدۡعُ نَادِيَهٗ ۙ", "falyad‘u nâdiyah", "Biarlah dia memanggil golongannya (untuk menolongnya).","alalaq_ayat17"),
                    Ayat("١٨", "سَنَدۡعُ الزَّبَانِيَةَ ۙ", "sanad‘uz-zabâniyah", "Kelak Kami akan memanggil (Malaikat) Zabaniah (penyiksa orang-orang yang berdosa).","alalaq_ayat18"),
                    Ayat("١٩", "كَلَّا ؕ لَا تُطِعۡهُ وَاسۡجُدۡ وَاقۡتَرِبْ", "kallâ, lâ tuthi‘hu wasjud waqtarib", "Sekali-kali tidak! Janganlah patuh kepadanya, (tetapi) sujud dan mendekatlah (kepada Allah).","alalaq_ayat19")
                    )
            ),
            Surah(
                "Al-Qoriah", "٢","Hari Kiamat", listOf(
                    Ayat("١", "اَلۡقَارِعَةُ ۙ", "al-qâri‘ah", "Al-Qāri‘ah (hari Kiamat yang menggetarkan).","alqariah_ayat1"),
                    Ayat("٢", "مَا الۡقَارِعَةُؕ", "mal-qâri‘ah", "Apakah al-Qāri‘ah itu?","alqariah_ayat2"),
                    Ayat("٣", "وَمَاۤ اَدۡرٰٮكَ مَا الۡقَارِعَةُؕ", "wa mâ adrâka mal-qâri‘ah", "Tahukah kamu apakah al-Qāri‘ah itu?","alqariah_ayat3"),
                    Ayat("٤", "يَوۡمَ يَكُوۡنُ النَّاسُ كَالۡفَرَاشِ الۡمَبۡثُوۡثَِۙ", "yauma yakûnun-nâsu kal-farâsyil-mabtsûts", "Pada hari itu manusia seperti laron yang beterbangan","alqariah_ayat4"),
                    Ayat("٥", "وَتَكُوۡنُ الۡجِبَالُ كَالۡعِهۡنِ الۡمَنۡفُوۡشِؕ", "wa takûnul-jibâlu kal-‘ihnil-manfûsy", "dan gunung-gunung seperti bulu yang berhamburan.","alqariah_ayat5"),
                    Ayat("٦", "فَاَمَّا مَنۡ ثَقُلَتۡ مَوَازِيۡنُهٗ ۙ", "fa ammâ man tsaqulat mawâzînuh", "Siapa yang berat timbangan (kebaikan)-nya,","alqariah_ayat6"),
                    Ayat("٧", "فَهُوَ فِىۡ عِيۡشَةٍ رَّاضِيَةٍؕ", "fa huwa fî ‘îsyatir râdliyah", "dia berada dalam kehidupan yang menyenangkan.","alqariah_ayat7"),
                    Ayat("٨", "وَاَمَّا مَنۡ خَفَّتۡ مَوَازِيۡنُهٗ ۙ", "wa ammâ man khaffat mawâzînuh", "Adapun orang yang ringan timbangan (kebaikan)-nya,","alqariah_ayat8"),
                    Ayat("٩", "فَاُمُّهٗ هَاوِيَةٌؕ", "fa ummuhû hâwiyah", "tempat kembalinya adalah (neraka) Hawiyah.","alqariah_ayat9"),
                    Ayat("١٠", "وَمَاۤ اَدۡرٰٮكَ مَا هِيَهۡؕ", "wa mâ adrâka mâ hiyah", "Tahukah kamu apakah (neraka Hawiyah) itu?","alqariah_ayat10"),
                    Ayat("١١", "نَارٌ حَامِيَةٌ", "nârun ḫâmiyah", "(Ia adalah) api yang sangat panas.","alqariah_ayat11")
                    )
            ),
            Surah(
                "Al-Humazah", "٣","Pengumpat", listOf(
                    Ayat("١", "وَيۡلٌ لِّـكُلِّ هُمَزَةٍ لُّمَزَةِ ۙ", "wailul likulli humazatil lumazah", "Celakalah setiap pengumpat lagi pencela","alhumazah_ayat1"),
                    Ayat("٢", "اۨلَّذِىۡ جَمَعَ مَالًا وَّعَدَّدَهٗ َۙ", "alladzî jama‘a mâlaw wa ‘addadah", "yang mengumpulkan harta dan menghitung-hitungnya.","alhumazah_ayat2"),
                    Ayat("٣", "يَحۡسَبُ اَنَّ مَالَهٗۤ اَخۡلَدَهَٗ", "yaḫsabu anna mâlahû akhladah", "Dia (manusia) mengira bahwa hartanya dapat mengekalkannya.","alhumazah_ayat3"),
                    Ayat("٤", "كَلَّا لَيُنۡۢبَذَنَّ فِى الۡحُطَمَةَِِۙ", "kall â layumbadzanna fil-ḫuthamah", "Sekali-kali tidak! Pasti dia akan dilemparkan ke dalam (neraka) Hutamah.","alhumazah_ayat4"),
                    Ayat("٥", "وَمَاۤ اَدۡرٰٮكَ مَا الۡحُطَمَةُؕ", "wa mâ adrâka mal-ḫuthamah", "Tahukah kamu apakah (neraka) Hutamah?","alhumazah_ayat5"),
                    Ayat("٦", "نَارُ اللّٰهِ الۡمُوۡقَدَةُ ۙ", "nârullâhil-mûqadah", "(Ia adalah) api (azab) Allah yang dinyalakan","alhumazah_ayat6"),
                    Ayat("٧", "الَّتِىۡ تَطَّلِعُ عَلَى الۡاَفۡـــِٕدَةِ ؕ", "allatî taththali‘u ‘alal-af'idah", "yang (membakar) naik sampai ke hati.","alhumazah_ayat7"),
                    Ayat("٨", "اِنَّهَا عَلَيۡهِمۡ مُّؤۡصَدَةٌ ۙ", "innahâ ‘alaihim mu'shadah", "Sesungguhnya dia (api itu) tertutup rapat (sebagai hukuman) atas mereka,","alhumazah_ayat8"),
                    Ayat("٩", "فِىۡ عَمَدٍ مُّمَدَّدَةٍ", "fî ‘amadim mumaddadah", "(sedangkan mereka) diikat pada tiang-tiang yang panjang.","alhumazah_ayat9")
                )
            )
        )
    }

}