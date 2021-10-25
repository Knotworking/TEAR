package com.knotworking.data.map

import android.content.Context
import android.util.Log
import com.knotworking.data.R
import com.knotworking.data.map.file.NavigationDataSet
import com.knotworking.data.map.file.NavigationSaxHandler
import com.knotworking.data.map.inmemory.BalkanKmMarkers
import org.xml.sax.InputSource
import org.xml.sax.XMLReader
import java.io.InputStream
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory

interface MapDataSource {
    suspend fun parseMapData()
}

class InMemoryMapDataSource() : MapDataSource {
    override suspend fun parseMapData() {
        val start = System.currentTimeMillis()
        val data = BalkanKmMarkers.markerLocations
        Log.i("TAG", "data: ${data.size}")
        val timeTaken = System.currentTimeMillis() - start
        println("ms taken = $timeTaken")
    }

}

class LocalMapDataSource(private val context: Context) : MapDataSource {
    override suspend fun parseMapData() {
        val start = System.currentTimeMillis()
        val navData = getNavigationDataSet()
        if (navData != null) {
            Log.i("TAG", "nav data: ${navData.getPlacemarks().size}")
            val timeTaken = System.currentTimeMillis() - start
            println("ms taken = $timeTaken")
            val routeCoords = navData.routeData.first().split("\n")


            routeCoords.forEach {
                println("Location(${it.split(",").take(2).joinToString(",")}),")
            }
        } else {
            Log.i("TAG", "nav data is null")
        }
    }

    private fun getNavigationDataSet(): NavigationDataSet? {
        var navigationDataSet: NavigationDataSet? = null
        navigationDataSet = try {
            /* Get a SAXParser from the SAXParserFactory. */
            val spf: SAXParserFactory = SAXParserFactory.newInstance()
            val sp: SAXParser = spf.newSAXParser()

            /* Get the XMLReader of the SAXParser we created. */
            val xr: XMLReader = sp.xmlReader

            /* Create a new ContentHandler and apply it to the XML-Reader*/
            val navSax2Handler = NavigationSaxHandler()
            xr.contentHandler = navSax2Handler
            /* Parse the xml-data from our URL. */
//            val stream: InputStream =
//                context.resources.openRawResource(R.raw.balkan_mountains_km_markers)

            val stream: InputStream =
                context.resources.openRawResource(R.raw.balkan_mountains_main_route)

            xr.parse(InputSource(stream))
            /* Our NavigationSaxHandler now provides the parsed data to us. */
            navSax2Handler.parsedData
        } catch (e: Exception) {
            Log.e("TAG", "error getting route info", e)
            null
        }
        return navigationDataSet
    }
}