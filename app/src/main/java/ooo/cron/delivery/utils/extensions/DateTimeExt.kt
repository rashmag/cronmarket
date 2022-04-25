package ooo.cron.delivery.utils.extensions

import android.content.Context
import android.text.format.DateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue

/**
 * удобрый метод чтобы мокнуть его в тестах
 * @return Возвращает текущую дату в виде ZonedDateTime
 */
fun currentZonedDateTime(): ZonedDateTime = ZonedDateTime.now()
fun currentLocalTime(): LocalTime = LocalTime.of(LocalTime.now().hour, LocalTime.now().minute)

val mskServerZoneId: ZoneId = ZoneId.of("Europe/Moscow")

val serverZoneId: ZoneId = ZoneId.of("GMT+0")

/**
 * Таймзона выставленная на телефоне
 */
val localZoneId: ZoneId = ZoneId.systemDefault()

/**
 * Сахар вокруг DateTimeFormatter
 */
fun ZonedDateTime.format(pattern: String): String = DateTimeFormatter.ofPattern(pattern).format(this)

/**
 * Возвращает просто время в формате 12/24 часа в зависимости от настроек телефона
 * @param context == null то всегда в формате 24 часа
 */
fun ZonedDateTime?.formatTimeOnly(context: Context? = null): String {
    this ?: return ""

    if (context != null && !DateFormat.is24HourFormat(context)) {
        return this.format("h:mm a")
    }

    return this.format("HH:mm")
}

/**
 * Перевести ZonedDateTime в таймзону Москвы
 */
fun ZonedDateTime.asMoscowTime(): ZonedDateTime = this.withZoneSameInstant(mskServerZoneId)

/**
 * Перевести ZonedDateTime в таймзону телефона
 */
fun ZonedDateTime.asLocalTime(): ZonedDateTime = this.withZoneSameInstant(ZoneId.systemDefault())

/**
 * Перевести ZonedDateTime в "GMT+0"
 */
fun ZonedDateTime.asServerTimezone(): ZonedDateTime = this.withZoneSameInstant(serverZoneId)

/**
 * @return true если дата совпадает с сегодняшней, в текущей таймзоне
 * прокидываем в LocalDate таймзону из ZonedDateTime
 */
fun ZonedDateTime.isToday(): Boolean = this.toLocalDate() == LocalDate.now(this.zone)

/**
 * @return true если дата совпадает с завтрашней, в текущей таймзоне
 * прокидываем в LocalDate таймзону из ZonedDateTime
 */
fun ZonedDateTime.isTomorrow(): Boolean = this.toLocalDate() == LocalDate.now(this.zone).plusDays(1)

/**
 * Сравнивает два ZonedDateTime с учётом из таймзон
 */
fun ZonedDateTime?.isOneDay(anotherDate: ZonedDateTime?): Boolean {

    if (this == null || anotherDate == null)
        return false

    return this.asLocalTime().toLocalDate() == anotherDate.asLocalTime().toLocalDate()
}

/**
 * Возвращает время в милисекундах от 1.01.1970
 * Аналог time у Date
 */
val ZonedDateTime.time: Long
    get() = this.toInstant().toEpochMilli()

/**
 * @return возвращает разницу в минутах между ZonedDateTime и текущим временем телефона
 */
fun ZonedDateTime.diffMinutes(): Long {
    val valueInSecond = this.toEpochSecond() - currentZonedDateTime().toEpochSecond()
    return TimeUnit.SECONDS.toMinutes(valueInSecond)
}

/**
 * @return возвращает разницу в указанном измерении (дни, минуты и т.д) между одной ZonedDateTime и другой
 */
fun ZonedDateTime.diffBetween(secondDateTime: ZonedDateTime, unit: ChronoUnit, daysInclusive: Boolean = false): Long {
    return unit.between(this, secondDateTime).apply {
        if (daysInclusive) plus(1) // в случае, когда нужно количество дней в промежутке, а не разница
    }
}

/**
 * @return парсит из строки дату/время в указанном формате
 */
fun String.parseDateTimeByPattern(pattern: String): ZonedDateTime {
    return ZonedDateTime.parse(this, DateTimeFormatter.ofPattern(pattern))
}

fun String.parseTime(): LocalTime {
    return LocalTime.parse(this)
}

/**
 * @return возвращает массив дат в промежутке с указанным интервалом
 * @param endAt - дата закрытия
 * @param periodValue - интервал между временными отрезками
 * @param unit - единицы измерения (на перспективу), сейчас по дефолту "минуты"
 */
fun LocalTime.timeBetweenIterator(
    endAt: LocalTime,
    periodValue: Long = 10,
    unit: ChronoUnit = ChronoUnit.MINUTES
): ArrayList<LocalTime> {

    val startAt = this
    val currentTime = currentLocalTime()
    val isCloseAtNextDay = startAt > endAt // проверка на случай, если время закрытия на след. день (например в 03:00)

    val midnight = LocalTime.of(0, 0)
    val endOfDay = LocalTime.of(23, 59)

    /** проверяем открыто ли заведение сейчас */
    val isOpenNow = when {
        // для круглосуточных заведений
        startAt == endAt -> true
        // попадает в промежуток (например с 10:00 до 03:00)
        isCloseAtNextDay && (currentTime in startAt..endOfDay || currentTime in midnight..endAt) -> true
        // попадает в промежуток (например с 10:00 до 22:00)
        currentTime in startAt..endAt -> true
        else -> false // закрыто
    }

    /** задаем начальное время */
    var nextTime = when {
        // если открыто, то берем текущее время, округляем минуты и добавляем +1 час
        isOpenNow -> currentTime.timeRoundMinutes(periodValue).plusHours(1)
        else -> {
            val beforeTime = unit.between(currentTime, startAt).absoluteValue
            if (beforeTime < 60) {
                // если до открытия меньше часа, то округляем минуты и прибавляем +1 час
                currentTime.timeRoundMinutes(periodValue).plusHours(1)
            } else {
                // иначе берем просто время открытия
                startAt
            }
        }
    }

    val list = arrayListOf(nextTime)

    /**
     * заполняем массив от начального времени до времени закрытия с заданным интервалом
     * @param nextTime - начальное время
     * @param endAt - время закрытия
     */
    while (nextTime != endAt.timeRoundMinutes(periodValue)) {
        nextTime = nextTime.plus(periodValue, unit)
        list.add(nextTime)
    }

    return list
}

/**
 * @return округляет минуты - например. если интервал = 10 и время 10:12, то округляем до 10:20
 * @param periodValue - значения интервала (сейчас по дефолту 10)
 */
fun LocalTime.timeRoundMinutes(periodValue: Long): LocalTime {
    val minute = this.minute
    return if (minute % periodValue == 0L) this
    else {
        val roundedMinutes = ((minute + periodValue)-(minute % periodValue)).toInt()
        if (roundedMinutes >= 60) this.plusHours(1).withMinute(0)
        else this.withMinute(roundedMinutes)
    }
}

/**
 * Возвращает просто время в формате 12/24 часа в зависимости от настроек телефона
 * @param context == null то всегда в формате 24 часа
 */
fun LocalTime?.formatShortTime(context: Context? = null): String {
    this ?: return ""

    if (context != null && !DateFormat.is24HourFormat(context)) {
        return this.format(DateTimeFormatter.ofPattern("h:mm a"))
    }

    return this.format(DateTimeFormatter.ofPattern("HH:mm"))
}