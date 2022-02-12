package de.test.antennapod.playback;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import de.danoeh.antennapod.activity.MainActivity;
import de.danoeh.antennapod.core.preferences.UserPreferences;
import de.danoeh.antennapod.core.util.playback.PlaybackController;
import de.test.antennapod.EspressoTestUtils;
import de.test.antennapod.IgnoreOnCi;
import de.test.antennapod.ui.UITestUtils;

/**
 * Test cases for starting and ending playback from the MainActivity and AudioPlayerActivity.
 */
@LargeTest
@IgnoreOnCi
@RunWith(Parameterized.class)
public class PlaybackTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class, false, false);

    @Parameterized.Parameter(value = 0)
    public String playerToUse;
    private UITestUtils uiTestUtils;
    protected Context context;
    private PlaybackController controller;

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> initParameters() {
        return Arrays.asList(new Object[][] { { "exoplayer" }, { "builtin" }, { "sonic" } });
    }

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        EspressoTestUtils.clearPreferences();
        EspressoTestUtils.clearDatabase();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(UserPreferences.PREF_MEDIA_PLAYER, playerToUse).apply();

        uiTestUtils = new UITestUtils(context);
        uiTestUtils.setup();
    }

    @After
    public void tearDown() throws Exception {
        activityTestRule.finishActivity();
        EspressoTestUtils.tryKillPlaybackService();
        uiTestUtils.tearDown();
        if (controller != null) {
            controller.release();
        }
    }

}
