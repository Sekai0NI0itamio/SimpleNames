package com.simplenames.simplenames;

import net.minecraft.ChatFormatting;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NameColorsTest {
    @Test
    public void parsesOnlyRealColors() {
        assertEquals(ChatFormatting.RED, NameColors.parse("red"));
        assertEquals(ChatFormatting.RED, NameColors.parse("RED"));
        assertNull(NameColors.parse("bold"));
        assertNull(NameColors.parse("obfuscated"));
        assertNull(NameColors.parse("notacolor"));
        assertNull(NameColors.parse(null));
    }

    @Test
    public void teamNamesAreShortAndUnique() {
        String first = NameColors.teamName(UUID.randomUUID());
        String second = NameColors.teamName(UUID.randomUUID());
        assertTrue(first.length() <= 16);
        assertTrue(!first.equals(second));
    }

    @Test
    public void colorListIsNotEmpty() {
        assertNotNull(NameColors.COLORS);
        assertTrue(NameColors.COLORS.contains("red"));
    }
}
