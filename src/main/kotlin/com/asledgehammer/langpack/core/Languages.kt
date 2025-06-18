@file:Suppress("MemberVisibilityCanBePrivate", "unused", "SpellCheckingInspection")

package com.asledgehammer.langpack.core

import java.util.*

/**
 * **Languages** is a collection-singleton designed to both manage
 * registered [Language] objects for [LangPack]. The option for adding
 * additional languages is supported.
 *
 * @author Jab
 */
object Languages {

  /** An immutable collection of all registered Languages. */
  var values = emptyList<Language>()
    private set

  private val mapLocale = HashMap<Locale, Language>()
  private val mapString = HashMap<String, Language>()

  /**
   * Attempts to resolve a registered language that identifies with a
   * raw locale string. If none identify, the fallback language passed
   * is returned.
   *
   * @param rawLocale The raw locale to test. Raw locales are either
   *     '**language**' or '**language**_**region**'.
   * @param fallback The fallback language to use if the rawLocale fails
   *     to identify with a registered language.
   * @return Returns the closest language. If no languages identify with
   *     the rawLocale, the defaultLanguage is returned.
   */
  fun getClosest(rawLocale: String, fallback: Language): Language =
    getClosest(toLocale(rawLocale), fallback)

  /**
   * Attempts to resolve a registered language that identifies with a
   * locale. If none identify, the fallback language passed is returned.
   *
   * @param locale The raw locale to test.
   * @param fallback The fallback language to use if the rawLocale fails
   *     to identify with a registered language.
   * @return Returns the closest language. If no languages identify with
   *     the locale, the defaultLanguage is returned.
   */
  fun getClosest(locale: Locale, fallback: Language): Language {
    var language: Language? = get(locale)
    if (language != null) return language
    language = search(locale, language = true, region = true)
    if (language != null) return language
    language = search(locale, language = true)
    if (language != null) return language
    return fallback
  }

  /**
   * @param locale The locale to test.
   * @return Returns the language that identifies with the locale. If
   *     none identifies with the locale, null is returned.
   */
  fun get(locale: Locale): Language? {
    for ((nextLocale, language) in mapLocale) if (locale == nextLocale) return language
    return null
  }

  /**
   * @param rawLocale The raw locale of the Language.
   * @return Returns the Language that identifies with the given
   *     abbreviation. If none identifies, null is returned.
   */
  fun get(rawLocale: String): Language? {
    val lower = rawLocale.lowercase(Locale.getDefault())
    for ((nextRawLocale, language) in mapString) if (nextRawLocale == lower) return language
    return null
  }

  /**
   * Registers a raw locale string as a [Locale] instance, with a raw
   * locale as a fallback.
   *
   * **NOTE:** Make sure to register the fallback language first before
   * registering sub-languages that fall back on it.
   *
   * @param rawLocale The raw locale to register.
   * @param rawLocaleFallback The raw local of the fallback language to
   *     set.
   * @return Returns the constructed language.
   */
  fun register(rawLocale: String, rawLocaleFallback: String? = null): Language {
    require(rawLocale.isNotEmpty()) { "The raw locale is empty." }
    require(rawLocaleFallback == null || rawLocaleFallback.isNotEmpty()) { "The fallback raw locale is empty." }

    val locale: Locale = toLocale(rawLocale)
    val fallback: Language? = if (rawLocaleFallback != null) mapLocale[toLocale(rawLocaleFallback)] else null
    val language = Language(locale, fallback)
    register(language)
    return language
  }

  /**
   * Registers a locale with a fallback language.
   *
   * @param locale The locale to register.
   * @param fallback The fallback language to set for the locale.
   * @return Returns the constructed language.
   */
  fun register(locale: Locale, fallback: Language? = null): Language {
    val language = Language(locale, fallback)
    register(language)
    return language
  }

  /**
   * Registers a Language instance.
   *
   * @param language The language to register.
   */
  fun register(language: Language) {
    mapLocale[language.locale] = language
    mapString[language.rawLocale.lowercase(Locale.getDefault())] = language
    buildValues()
  }

//    /**
//     * Unregisters a language.
//     *
//     * @param language
//     */
//    fun unregister(language: Language) {
//        mapLocale.remove(language.locale)
//        mapString.remove(language.rawLocale.toLowerCase())
//        buildValues()
//    }

  /**
   * Converts a raw locale string into a [Locale] instance.
   *
   * @param raw The raw locale string to convert.
   * @return Returns the constructed locale instance.
   */
  fun toLocale(raw: String): Locale {
    return if (raw.contains("_")) {
      val split = raw.lowercase(Locale.getDefault()).split("_")
      Locale(split[0], split[1])
    } else {
      Locale(raw)
    }
  }

  /**
   * Attempts to locate a language by comparing a locale with registered
   * locales.
   *
   * @param locale the locale to test.
   * @param language Set this to true to test the language of each
   *     registered locale.
   * @param region Set this to true to test the region of each
   *     registered locale.
   * @return Returns the first registered language to match the criteria
   *     tested. If no matches are found, null is returned.
   */
  private fun search(locale: Locale, language: Boolean, region: Boolean = false): Language? {
    for ((next, nextLanguage) in mapLocale) {
      if ((!language || next.language == locale.language) && (!region || next.country == locale.country)) {
        return nextLanguage
      }
    }
    return null
  }

  /**
   * (Rebuilds the immutable list for accessing registered languages as
   * a collection.)
   */
  private fun buildValues() {
    values = Collections.unmodifiableList(ArrayList(mapLocale.values))
  }

  val AFRIKAANS_GENERIC: Language = register("af")
  val AFRIKAANS: Language = register("af_za", "af")
  val ARABIC_GENERIC: Language = register("ar")
  val ARABIC: Language = register("ar_sa", "ar")
  val AZERBAIJANI_GENERIC: Language = register("az")
  val AZERBAIJANI: Language = register("az_az", "az")
  val BOSNIAN_GENERIC: Language = register("bs")
  val BOSNIAN: Language = register("bs_ba", "bs")
  val CHINESE_GENERIC: Language = register("zh")
  val CHINESE_SIMPLIFIED: Language = register("zh_cn", "zh")
  val CHINESE_TRADITIONAL: Language = register("zh_tw", "zh")
  val CZECH_GENERIC: Language = register("cz")
  val CZECH: Language = register("cs_cz", "cz")
  val DANISH_GENERIC: Language = register("da")
  val DANISH: Language = register("da_dk", "da")
  val DUTCH_GENERIC: Language = register("nl")
  val DUTCH: Language = register("nl_nl", "nl")
  val DUTCH_FLEMISH: Language = register("nl_be", "nl")
  val ENGLISH_GENERIC: Language = register("en")
  val ENGLISH_UNITED_STATES: Language = register("en_us", "en")
  val ENGLISH_AUSTRALIA: Language = register("en_au", "en")
  val ENGLISH_CANADA: Language = register("en_ca", "en")
  val ENGLISH_UNITED_KINGDOM: Language = register("en_gb", "en")
  val ENGLISH_NEW_ZEALAND: Language = register("en_nz", "en")
  val ENGLISH_SOUTH_AFRICA: Language = register("en_za", "en")
  val ENGLISH_PIRATE_SPEAK: Language = register("en_pt", "en")
  val ENGLISH_UPSIDE_DOWN: Language = register("en_ud", "en")
  val ANGLISH: Language = register("enp", "en")
  val SHAKESPEAREAN: Language = register("enws", "en")
  val ESPERANTO_GENERIC: Language = register("eo")
  val ESPERANTO: Language = register("eo_uy", "eo")
  val ESTONIAN_GENERIC: Language = register("et")
  val ESTONIAN: Language = register("et_ee", "et")
  val FAROESE_GENERIC: Language = register("fo")
  val FAROESE: Language = register("fo_fo", "fo")
  val FILIPINO_GENERIC: Language = register("fil")
  val FILIPINO: Language = register("fil_ph", "fil")
  val FINNISH_GENERIC: Language = register("fi")
  val FINNISH: Language = register("fi_fi", "fi")
  val FRENCH_GENERIC: Language = register("fr")
  val FRENCH: Language = register("fr_fr", "fr")
  val FRENCH_CANADAIAN: Language = register("fr_ca", "fr")
  val BRETON: Language = register("br_fr", "fr")
  val FRISIAN_GENERIC: Language = register("fy")
  val FRISIAN: Language = register("fy_nl", "fy")
  val GERMAN_GENERIC: Language = register("de")
  val GERMAN: Language = register("de_de", "de")
  val AUSTRIAN: Language = register("de_at", "de")
  val SWISS: Language = register("de_ch", "de")
  val EAST_FRANCONIAN: Language = register("fra_de", "de")
  val LOW_GERMAN: Language = register("nds_de", "de")
  val GREEK_GENERIC: Language = register("gr")
  val GREEK: Language = register("el_gr", "gr")
  val INDONESIAN_GENERIC: Language = register("id")
  val INDONESIAN: Language = register("id_id", "id")
  val IRISH_GENERIC: Language = register("ga")
  val IRISH: Language = register("ga_ie", "ga")
  val ITALIAN_GENERIC: Language = register("it")
  val ITALIAN: Language = register("it_it", "it")
  val JAPANESE_GENERIC: Language = register("jp")
  val JAPANESE: Language = register("ja_jp", "jp")
  val KABYLE_GENERIC: Language = register("kab")
  val KABYLE: Language = register("kab_kab", "kab")
  val KOREAN_GENERIC: Language = register("kr")
  val KOREAN_HANGUG: Language = register("ko_kr", "kr")
  val LATIN_GENERIC: Language = register("la")
  val LATIN: Language = register("la_la", "la")
  val LATVIAN_GENERIC: Language = register("lv")
  val LATVIAN: Language = register("lv_lv")
  val LIMBURGISH_GENERIC: Language = register("li")
  val LIMBURGISH: Language = register("li_li", "li")
  val LITHUANIAN_GENERIC: Language = register("lt")
  val LITHUANIAN: Language = register("lt_lt", "lt")
  val MACEDONIAN_GENERIC: Language = register("mk")
  val MACEDONIAN: Language = register("mk_mk", "mk")
  val MALTESE_GENERIC: Language = register("mt")
  val MALTESE: Language = register("mt_mt", "mt")
  val MONGOLIAN_GENERIC: Language = register("mn")
  val MONGOLIAN: Language = register("mn_mn", "mn")
  val PERSIAN_GENERIC: Language = register("fa")
  val PERSIAN: Language = register("fa_ir")
  val POLISH_GENERIC: Language = register("pl")
  val POLISH: Language = register("pl_pl", "pl")
  val PORTUGUESE_GENERIC: Language = register("pt")
  val PORTUGUESE: Language = register("pt_pt", "pt")
  val ROMANIAN_GENERIC: Language = register("ro")
  val ROMANIAN: Language = register("ro_ro")
  val RUSSIAN_GENERIC: Language = register("ru")
  val RUSSIAN: Language = register("ru_ru", "ru")
  val BASHKIR: Language = register("ba_ru", "ru")
  val BELARUSIAN: Language = register("be_by", "ru")
  val BULGARIAN: Language = register("bg_bg", "ru")
  val SCOTTISH_GENERIC: Language = register("gd")
  val SCOTTISH_GAELIC: Language = register("gd_gb", "gd")
  val SOMALI_GENERIC: Language = register("so")
  val SOMALI: Language = register("so_so", "so")
  val ESPANOL_GENERIC: Language = register("es")
  val ESPANOL_ARGENTINA: Language = register("es_ar", "es")
  val ESPANOL_CHILE: Language = register("es_cl", "es")
  val ESPANOL_EQUADOR: Language = register("es_ec", "es")
  val ESPANOL_ESPANA: Language = register("es_es", "es")
  val ESPANOL_MEXICO: Language = register("es_mx", "es")
  val ESPANOL_URUGUAY: Language = register("es_uy", "es")
  val ESPANOL_VENEZUELA: Language = register("es_ve", "es")
  val ASTURIAN: Language = register("ast_es", "es")
  val BASQUE: Language = register("eu_es", "es")
  val CATALAN: Language = register("ca_es", "es")
  val THAI_GENERIC: Language = register("th")
  val THAI: Language = register("th_th", "th")
  val TURKISH_GENERIC: Language = register("tr")
  val TURKISH: Language = register("tr_tr", "tr")
  val WELSH_GENERIC: Language = register("cy")
  val WELSH: Language = register("cy_gb", "cy")
  val ANDALUSIAN: Language = register("esan")
  val BAVARIAN: Language = register("bar")
  val BRABANTIAN: Language = register("brb")
  val GALICIAN: Language = register("gl_es")
  val GOTHIC: Language = register("got_de")
  val ALBANIAN: Language = register("sq_al")
  val ALLGOVIAN_GERMAN: Language = register("swg")
  val ARMENIAN: Language = register("hy_am")
  val BRAZILIAN_PORTUGUESE: Language = register("pt_br", "pt")
  val CORNISH: Language = register("kw_gb")
  val CROATIAN: Language = register("hr_hr")
  val ELFDALIAN: Language = register("ovd")
  val GEORGIAN: Language = register("ka_ge")
  val HAWAIIAN: Language = register("haw_us")
  val HEBREW: Language = register("he_il")
  val HINDI: Language = register("hi_in")
  val HUNGARIAN: Language = register("hu_hu")
  val ICELANDIC: Language = register("is_is")
  val IDO: Language = register("io_en")
  val IGBO: Language = register("ig_ng")
  val INTERSLAVIC: Language = register("isv")
  val KANNADA: Language = register("kn_in")
  val KAZAKH: Language = register("kk_kz")
  val KLINGON: Language = register("til_aa")
  val KOLSCH_RIPUARIAN: Language = register("ksh")
  val LOJBAN: Language = register("jbo_en")
  val LOLCAT: Language = register("lol_us")
  val LUXEMBOURGISH: Language = register("lb_lu")
  val MALAY: Language = register("ms_my")
  val MANX: Language = register("gv_im")
  val MOHAWK: Language = register("moh_ca")
  val MAON: Language = register("mi_nz")
  val NORTHERN_SAMI: Language = register("sme")
  val NORTHERN_BOKMAL: Language = register("nb_no")
  val NORWEGIAN_NYNORSK: Language = register("nn_no")
  val NUUCHAHNULTH: Language = register("nuk")
  val OCCITAN: Language = register("oc_fr")
  val OJIBWE: Language = register("oj_ca")
  val QUENYA: Language = register("qya_aa")
  val SICILIAN: Language = register("scn")
  val SLOVAK: Language = register("sk_sk")
  val SLOVENIAN: Language = register("sl_si")
  val SERBIAN: Language = register("sr_sp")
  val SWEDISH: Language = register("sv_se")
  val UPPER_SAXON_GERMAN: Language = register("sxu")
  val SILESIAN: Language = register("szl")
  val TAMIL: Language = register("ta_in")
  val TATAR: Language = register("tt_ru")
  val TALOSSAN: Language = register("tzl_tzl")
  val UKRAINIAN: Language = register("uk_ua")
  val VALENCIAN: Language = register("val_es")
  val VENETIAN: Language = register("vec_it")
  val VIETNAMESE: Language = register("vi_vn")
  val YIDDISH: Language = register("yi_de")
  val YORUBA: Language = register("yo_ng")
}
