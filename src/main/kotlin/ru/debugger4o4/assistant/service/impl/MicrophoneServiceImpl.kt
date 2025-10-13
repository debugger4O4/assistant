package ru.debugger4o4.assistant.service.impl

import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import org.springframework.stereotype.Service
import ru.debugger4o4.assistant.service.MicrophoneService
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine
import java.io.File

@Service
class MicrophoneServiceImpl(private val fileName: String = "record/recording.wav") : MicrophoneService {

    private var audioInputStream: TargetDataLine? = null
    private lateinit var outputFile: File

    override fun getMicrophones(): String {
        var response = ""
        val mixers = AudioSystem.getMixerInfo()
        mixers.forEachIndexed { index, mixer ->
            response += "${index + 1}. ${mixer.name}</br>"
        }
        return response
    }

    override fun startRecord() {
        println("Запуск записи...")
        try {
            // Получаем аудиоформат (стандартный PCM)
            val format = AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                44100f, // частота дискретизации
                16, // битовая глубина
                2, // количество каналов (стерео)
                4, // размер фрейма
                44100f, // скорость передачи данных
                false // bigEndian
            )

            val info = DataLine.Info(TargetDataLine::class.java, format)
            if (!AudioSystem.isLineSupported(info)) throw Exception("Звукозапись не поддерживается")

            audioInputStream = AudioSystem.getTargetDataLine(format) as TargetDataLine
            audioInputStream!!.open(format)
            audioInputStream!!.start()

            outputFile = File(fileName)

            val tempAudioInputStream = AudioInputStream(audioInputStream!!)
            AudioSystem.write(tempAudioInputStream, AudioFileFormat.Type.WAVE, outputFile)
            print(readAudioFile(fileName))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun readAudioFile(filePath: String) {
        try {
            val file = File(filePath)
            val inputStream = AudioSystem.getAudioInputStream(file)

            val format = inputStream.format
            val durationInSeconds = calculateDuration(inputStream.frameLength, format.frameRate.toDouble())

            println("Формат аудиофайла:")
            println("Тип аудиофайла: ${inputStream.markSupported()}")
            println("Количество каналов: ${format.channels}")
            println("Частота дискретизации: ${format.sampleRate} Гц")
            println("Длительность воспроизведения: $durationInSeconds сек.")
            println("Размер кадра: ${format.frameSize} байт")
            println("Битовая глубина: ${format.sampleSizeInBits}-бит")

            inputStream.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun stopRecord() {
        println("Завершение записи.")
        audioInputStream?.stop()
        audioInputStream?.close()
    }

    private fun calculateDuration(frameCount: Long, frameRate: Double): Double {
        return if (frameRate <= 0 || frameCount <= 0) {
            0.0
        } else {
            frameCount / frameRate
        }
    }
}