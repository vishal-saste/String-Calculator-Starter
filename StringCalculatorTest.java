package pl.calculations;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class StringCalculatorTest {

    private StringCalculator stringCalculator;

    @Before
    public void setUp() {
        stringCalculator = new StringCalculator();
    }

    @Test
    public void should_return_0_when_passed_string_is_empty() {
        assertThat(stringCalculator.add("")).isEqualTo(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_IllegalArgumentException_when_passed_string_is_not_a_number() {
        stringCalculator.add("1,s,3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_IllegalArgumentException_when_passed_string_has_comma_as_a_first_character() {
        stringCalculator.add(",1,2,3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_IllegalArgumentException_when_passed_string_has_duplicated_commas() {
        stringCalculator.add("1,,3");
    }

    @Test
    public void should_return_sum_when_passed_string_has_comma_as_last_character() {
        assertThat(stringCalculator.add("1,2,3,")).isEqualTo(1 + 2 + 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_IllegalArgumentException_when_passed_string_has_space_character() {
        stringCalculator.add("1,2, 3");
    }

    @Test
    public void should_throw_IllegalArgumentException_when_passed_string_has_tabulation_character() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> stringCalculator.add("1,2   ,3"));
    }

    @Test
    public void should_return_the_same_number_when_it_was_only_one_number_passed() {
        assertThat(stringCalculator.add("3")).isEqualTo(3);
    }

    @Test
    public void should_return_sum_of_every_amount_of_numbers_when_delimiter_is_only_comma() {
        assertThat(stringCalculator.add("3,2")).isEqualTo(3 + 2);
        assertThat(stringCalculator.add("3,2,6,10")).isEqualTo(3 + 2 + 6 + 10);
        assertThat(stringCalculator.add("3,2,6,10,100")).isEqualTo(3 + 2 + 6 + 10 + 100);
    }

    @Test
    public void should_return_sum_of_every_amount_of_numbers_when_delimiter_is_only_new_line() {
        assertThat(stringCalculator.add("3\n2")).isEqualTo(3 + 2);
        assertThat(stringCalculator.add("3\n2\n6\n10")).isEqualTo(3 + 2 + 6 + 10);
        assertThat(stringCalculator.add("3\n2\n6\n10\n100")).isEqualTo(3 + 2 + 6 + 10 + 100);
    }

    @Test
    public void should_return_sum_of_every_amount_of_numbers_when_delimiters_are_new_lines_and_commas() {
        assertThat(stringCalculator.add("3\n2,6\n10")).isEqualTo(3 + 2 + 6 + 10);
        assertThat(stringCalculator.add("3\n2,6\n10,100")).isEqualTo(3 + 2 + 6 + 10 + 100);
    }

    @Test
    public void should_return_sum_when_single_custom_delimiter_is_specified() {
        assertThat(stringCalculator.add("//[;]\n3;2;4")).isEqualTo(3 + 2 + 4);
        assertThat(stringCalculator.add("//[*]\n3*2*4")).isEqualTo(3 + 2 + 4);
    }

    @Test
    public void should_return_sum_when_single_type_and_any_length_custom_delimiter_is_specified() {
        assertThat(stringCalculator.add("//[ddd]\n3ddd2ddd4")).isEqualTo(3 + 2 + 4);
        assertThat(stringCalculator.add("//[***]\n3***2***4")).isEqualTo(3 + 2 + 4);
    }

    @Test
    public void should_return_sum_when_multiple_type_and_any_length_custom_delimiters_are_specified() {
        assertThat(stringCalculator.add("//[ddd][fff]\n3ddd2fff4")).isEqualTo(3 + 2 + 4);
        assertThat(stringCalculator.add("//[***][%]\n3***2%4")).isEqualTo(3 + 2 + 4);
        assertThat(stringCalculator.add("//[***][%][$$]\n3***2%4***5$$6%7")).isEqualTo(3 + 2 + 4 + 5 + 6 + 7);
    }


    @Test(expected = NumberFormatException.class)
    public void should_throw_NumberFormatException_when_delimiter_specification_is_separated_by_every_char_but_not_new_line() {
        stringCalculator.add("//;\t3;2;4");
    }

    @Test(expected = NumberFormatException.class)
    public void should_throw_NumberFormatException_when_delimiter_specification_is_not_separated_by_new_line() {
        stringCalculator.add("//;3;2;4");
    }

    @Test
    public void should_throw_IllegalArgumentException_when_negative_numbers_are_passed() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> stringCalculator.add("-2,3,-4"))
                .withMessage("Negatives not allowed: -2,-4");
    }

    @Test
    public void should_ignore_numbers_bigger_than_1000() {
        assertThat(stringCalculator.add("2,1001")).isEqualTo(2);
    }
}