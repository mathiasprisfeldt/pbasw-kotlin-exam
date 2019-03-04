package me.mathiasprisfeldt.blog.tests

import me.mathiasprisfeldt.blog.me.mathiasprisfeldt.blog.extensions.*
import org.junit.jupiter.api.Test
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*

class StringExtensionsTests {
    @Test
    fun `Word count test multiple words`() {
        val text = "Nu begynder det, dette er blot et test eksempel."
        assertThat(text.wordCount(), `is`(9))
    }

    @Test
    fun `Word count test single word`() {
        val text = "Ord"
        assertThat(text.wordCount(), `is`(1))
    }

    @Test
    fun `Word count no word(s)`() {
        val text = ""
        assertThat(text.wordCount(), `is`(0))
    }

    @Test
    fun `Word count only period and comma`() {
        val text = ".. . . . , ,, . , . ,. ,"
        assertThat(text.wordCount(), `is`(0))
    }

    @Test
    fun `Period count zero`() {
        val text = "Nu begynder noget test tekst"
        assertThat(text.periodCount(), `is`(0))
    }

    @Test
    fun `Period count multiple`() {
        val text = "Nu begynder. Noget Tekst. Som har. En masse. Punktummer."
        assertThat(text.periodCount(), `is`(5))
    }

    @Test
    fun `Word length count, length of 7 or greater`() {
        val text = "Nukommerderetlangtord men dog ogsaa nogle leangereendfoer"
        assertThat(text.wordLengthCount(7), `is`(2))
    }

    @Test
    fun `Word length count, length of 3`() {
        val text = "nogle mindre ord er der ogsaa, da det er med tallet 3"
        assertThat(text.wordLengthCount(3), `is`(8))
    }

    @Test
    fun `Lix number calculator, 'Let tekst for alle laesere'`() {
        val text = """
            Du vil jeg prøve at skrive. Noget som kan ramme et lix-tal som ligger på niveauet
            middel eller cirka deromkring. Det er dog ret svært at finde på nogle ord som er
            lange siden det nok er dem der gør tekst.
        """.trimMultiLine()

        assertThat(text.lix(), `is`(Pair(23, "Let tekst for alle læsere.")))
    }

    @Test
    fun `Lix number calculator, 'Let for oevede laesere'`() {
        val text = """
            Du vil jeg prøve at skrive. Noget som kan ramme et lix-tal som ligger på niveauet
            middel eller cirka deromkring. Det er dog ret svært at finde på nogle ord som er
            lange siden det nok er dem der gør teksten svær. Sådeertt awidjaiw.
        """.trimMultiLine()

        assertThat(text.lix(), `is`(Pair(27, "Let for øvede læsere.")))
    }

    @Test
    fun `Lix number calculator, 'Middel'`() {
        val text = """
            Du vil jeg prøve at skrive. Noget som kan ramme et lix-tal som ligger på niveauet
            middel eller cirka deromkring. Det er dog ret svært at finde på nogle ord som er
            lange siden det nok er dem der gør teksten svær. Så fotosyntsen, objektorienteret
            programmering. Respiration. Hovsajdawjw. ajidwjiawd. ajiwjad. aiwjiawd. awjidawjd.
            aidjaiwjd. ajwidjaiwdj. awjidjaw. jiawdjiaw. awidjwid.
        """.trimMultiLine()

        assertThat(text.lix(), `is`(Pair(37, "Middel.")))
    }

    @Test
    fun `Lix number calculator, 'Svear'`() {
        val text = """
            Kotlin is a language that supports both imperative, functional and object oriented
            programming. The official site is full of documentation.
        """.trimMultiLine()

        assertThat(text.lix(), `is`(Pair(50, "Svær.")))
    }

    @Test
    fun `Lix number calculator, 'Meget svear'`() {
        val text = """
            Den lille kernefamilie spiste røde knækpølser og drak en orangegul sodavand til og
            sluttede hele herligheden af med en stor vaffelis med flødebolle og mængder af krymmel,
            og bagefter pressede de sig læskede og overmætte ind i folkevognen og drønede af sted.
        """.trimMultiLine()

        assertThat(text.lix(), `is`(Pair(80, "Meget svær.")))
    }

    @Test
    fun `Lix number calculator, Tom tekst`() {
        val text = """.
        """.trimMultiLine()

        assertThat(text.lix(), `is`(Pair(0, "Teksten har ingen ord.")))
    }

    @Test
    fun `Lix number calculator, tekst uden punktummer`() {
        val text = """En tekst uden punktummer
        """.trimMultiLine()

        assertThat(text.lix(), `is`(Pair(0, "Teksten har ingen punktummer.")))
    }
}