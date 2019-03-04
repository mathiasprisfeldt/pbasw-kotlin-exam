package me.mathiasprisfeldt.blog.me.mathiasprisfeldt.blog.controllers

import me.mathiasprisfeldt.blog.BlogProperties
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class SequencesController(
        private val properties: BlogProperties) {

    @ModelAttribute("cfg")
    fun CfgSetup(): BlogProperties.Model {
        return properties.model
    }

    @GetMapping("/sequences")
    fun sequences(model: Model): String {
        return "sequences"
    }

    @PostMapping("/sequences")
    fun sequencesSubmit(@ModelAttribute form: SequencesForm): ModelAndView {
        val mv = ModelAndView("sequences")

        mv.addObject("isValid", form.isValid())
        mv.addObject("tableNumber", form.tableNumber)
        mv.addObject("tablePrintAmount", form.tablePrintAmount)
        mv.addObject("result", form.result())

        return mv
    }

    data class SequencesForm(
            val tableNumber: Int?,
            val tablePrintAmount: Int?
    ) {
        fun isValid(): Pair<Boolean, String> {
            var errMsg = "Input is illegal"

            if (tableNumber != null && tablePrintAmount != null)
                errMsg = ""

            return Pair(errMsg.isEmpty(), errMsg)
        }

        fun result(): String? {
            if (tableNumber == null || tablePrintAmount == null)
                return null

            val seq: Sequence<Int> = generateSequence(tableNumber) { it + tableNumber }

            return seq.take(tablePrintAmount).joinToString(", ")
        }

    }
}