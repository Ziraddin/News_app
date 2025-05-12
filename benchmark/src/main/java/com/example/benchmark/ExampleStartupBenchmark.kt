package com.example.benchmark

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.MemoryUsageMetric
import androidx.benchmark.macro.PowerMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@RunWith(AndroidJUnit4::class)
class ExampleStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = "com.example.news_app",
        metrics = listOf(StartupTimingMetric()),
        iterations = 1,
        startupMode = StartupMode.COLD,
        compilationMode = CompilationMode.None(),
    ) {
        pressHome()
        startActivityAndWait()
        testApp()
    }

    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun memoryUsage() = benchmarkRule.measureRepeated(
        packageName = "com.example.news_app",
        metrics = listOf(
            MemoryUsageMetric(
                mode = MemoryUsageMetric.Mode.Max, subMetrics = listOf(
                    MemoryUsageMetric.SubMetric.Gpu,
                    MemoryUsageMetric.SubMetric.RssAnon,
                    MemoryUsageMetric.SubMetric.RssFile,
                    MemoryUsageMetric.SubMetric.HeapSize,
                    MemoryUsageMetric.SubMetric.RssShmem,
                )
            )
        ),
        iterations = 1, startupMode = StartupMode.COLD,
        compilationMode = CompilationMode.None(),
    ) {
        testApp()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun powerUsage() = benchmarkRule.measureRepeated(
        packageName = "com.example.news_app",
        metrics = listOf(PowerMetric(type = PowerMetric.Battery())),
        iterations = 1,
        startupMode = StartupMode.COLD,
        compilationMode = CompilationMode.None(),
    ) {
        testApp()
    }

    @Test
    fun frameTimingMetric() = benchmarkRule.measureRepeated(
        packageName = "com.example.news_app",
        metrics = listOf(FrameTimingMetric()),
        iterations = 1,
        startupMode = StartupMode.COLD,
        compilationMode = CompilationMode.None(),
    ) {
        testApp()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun testAllMetrics() = benchmarkRule.measureRepeated(
        packageName = "com.example.news_app",
        metrics = listOf(
            StartupTimingMetric(), MemoryUsageMetric(
                mode = MemoryUsageMetric.Mode.Max, subMetrics = listOf(
                    MemoryUsageMetric.SubMetric.Gpu,
                    MemoryUsageMetric.SubMetric.RssAnon,
                    MemoryUsageMetric.SubMetric.RssFile,
                    MemoryUsageMetric.SubMetric.HeapSize,
                    MemoryUsageMetric.SubMetric.RssShmem,
                )
            ), PowerMetric(type = PowerMetric.Battery()), FrameTimingMetric()
        ),
        iterations = 20,
        startupMode = StartupMode.COLD,
        compilationMode = CompilationMode.None(),
    ) {
        testApp()
    }

    /**
     * The test performs the following actions:
     * 1. Presses the home button
     * 2. Starts the app
     * 3. Searches for "tesla"
     * 4. Scrolls the list
     * 5. Clicks on a news item
     * 6. Opens the news item in a browser
     * 7. Clicks on the bookmark icon
     * 8. Goes to the bookmark screen
     */

    private fun MacrobenchmarkScope.testApp() {
        pressHome()
        startActivityAndWait()
        searchForTesla()
        scrollList()
        clickNewsItem()
        openInBrowser()
        clickBookmark()
        goBookmarkScreen()
    }

    private fun MacrobenchmarkScope.searchForTesla() {
        val searchView = device.findObject(By.res("com.example.news_app:id/search_bar"))
        searchView?.click()
        device.waitForIdle()
        device.executeShellCommand("input text tesla")
        device.pressEnter()
        device.pressBack()
        device.waitForIdle()
    }


    private fun MacrobenchmarkScope.scrollList() {
        device.wait(Until.hasObject(By.res("com.example.news_app:id/recycler_view_news")), 3000)
        val list = device.findObject(By.res("com.example.news_app:id/recycler_view_news"))
        list.swipe(Direction.UP, 0.3f)
        device.waitForIdle()
        list.swipe(Direction.DOWN, 0.3f)
        device.waitForIdle()
        Thread.sleep(1000)
    }

    private fun MacrobenchmarkScope.clickNewsItem() {
        val newsItems = device.findObjects(By.res("com.example.news_app:id/news_item"))
        newsItems[0].click()
        Thread.sleep(1000)
        val articleDetailScreen =
            device.findObject(By.res("com.example.news_app:id/article_detail_screen"))
        articleDetailScreen.swipe(Direction.UP, 0.8f)
        device.waitForIdle()
    }

    private fun MacrobenchmarkScope.openInBrowser() {
        device.wait(Until.hasObject(By.res("com.example.news_app:id/urlTextView")), 3000)
        val openInBrowser = device.findObject(By.res("com.example.news_app:id/urlTextView"))
        openInBrowser.click()
        Thread.sleep(2000)
        repeat(2) {
            device.pressBack()
        }
        device.waitForIdle()
    }

    private fun MacrobenchmarkScope.clickBookmark() {
        device.wait(Until.hasObject(By.res("com.example.news_app:id/bookmarkIcon")), 3000)
        val bookmark = device.findObject(By.res("com.example.news_app:id/bookmarkIcon"))
        bookmark.click()
        device.waitForIdle()
    }

    private fun MacrobenchmarkScope.goBookmarkScreen() {
        device.wait(Until.hasObject(By.text("Bookmarks")), 3000)
        val bookmark = device.findObject(By.text("Bookmarks"))
        bookmark.click()
        device.waitForIdle()
    }
}