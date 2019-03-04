package me.mathiasprisfeldt.blog.me.mathiasprisfeldt.blog.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class SequencesController {

    @GetMapping("/sequences")
    fun getSequences(model: Model): String {
        return "sequences"
    }

    @PostMapping("/sequences")
    fun postSequences(@ModelAttribute form: SequencesForm): ModelAndView {
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
            var errMsg = ""

            if (tableNumber == null || tablePrintAmount == null)
                errMsg = "Input is illegal."

            return errMsg.isEmpty() to errMsg
        }

        fun result(): String? {
            if (tableNumber == null || tablePrintAmount == null)
                return null

            val seq: Sequence<Int> = generateSequence(tableNumber) { it + tableNumber }

            return seq.take(tablePrintAmount).joinToString(", ")
        }

    }
}