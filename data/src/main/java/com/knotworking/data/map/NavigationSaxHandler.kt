package com.knotworking.data.map

import android.util.Log
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler

// Adapted from code found online https://gist.github.com/RobGThai/3163006
class NavigationSaxHandler : DefaultHandler() {
    // ===========================================================
    // Fields
    // ===========================================================
    private var inKmlTag = false
    private var inPlacemarkTag = false
    private var inNameTag = false
    private var inDescriptionTag = false
    private var inGeometryCollectionTag = false
    private var inLineStringTag = false
    private var inPointTag = false
    private var inCoordinatesTag = false
    private var buffer: StringBuffer? = null
    private var navigationDataSet = NavigationDataSet()

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    val parsedData: NavigationDataSet
        get() {
            navigationDataSet.getCurrentPlacemark()!!.coordinates =
                buffer.toString().trim { it <= ' ' }
            return navigationDataSet
        }

    // ===========================================================
    // Methods
    // ===========================================================
    @Throws(SAXException::class)
    override fun startDocument() {
        navigationDataSet = NavigationDataSet()
    }

    @Throws(SAXException::class)
    override fun endDocument() {
        // Nothing to do
    }

    /** Gets be called on opening tags */
    @Throws(SAXException::class)
    override fun startElement(
        namespaceURI: String?, localName: String,
        qName: String?, atts: Attributes?
    ) {
        if (localName == "kml") {
            inKmlTag = true
        } else if (localName == "Placemark") {
            inPlacemarkTag = true
            Log.d("TAG", "startElement localName[" + localName + "] qName[" + qName + "]");
            navigationDataSet.setCurrentPlacemark(Placemark())
        } else if (localName == "name") {
            inNameTag = true
        } else if (localName == "description") {
            inDescriptionTag = true
        } else if (localName == "GeometryCollection") {
            inGeometryCollectionTag = true
        } else if (localName == "LineString") {
            inLineStringTag = true
        } else if (localName == "Point") {
            inPointTag = true
        } else if (localName == "coordinates") {
            buffer = StringBuffer()
            inCoordinatesTag = true
        }
    }

    /** Gets be called on closing tags */
    @Throws(SAXException::class)
    override fun endElement(namespaceURI: String?, localName: String, qName: String?) {
        if (localName == "kml") {
            inKmlTag = false
        } else if (localName == "Placemark") {
            inPlacemarkTag = false
            if ("Route" == navigationDataSet.getCurrentPlacemark()!!.name) navigationDataSet.routePlacemark =
                navigationDataSet.getCurrentPlacemark() else navigationDataSet.addCurrentPlacemark()
        } else if (localName == "name") {
            inNameTag = false
        } else if (localName == "description") {
            inDescriptionTag = false
        } else if (localName == "GeometryCollection") {
            inGeometryCollectionTag = false
        } else if (localName == "LineString") {
            inLineStringTag = false
        } else if (localName == "Point") {
            inPointTag = false
        } else if (localName == "coordinates") {
            inCoordinatesTag = false
        }
    }

    /** Gets be called on the following structure:
     * <tag>characters</tag>  */
    override fun characters(ch: CharArray?, start: Int, length: Int) {
        if (inNameTag) {
            if (navigationDataSet.getCurrentPlacemark() == null) navigationDataSet.setCurrentPlacemark(
                Placemark()
            )
            navigationDataSet.getCurrentPlacemark()!!.name = String(ch!!, start, length)
        } else if (inDescriptionTag) {
            if (navigationDataSet.getCurrentPlacemark() == null) navigationDataSet.setCurrentPlacemark(
                Placemark()
            )
            navigationDataSet.getCurrentPlacemark()!!.description = String(ch!!, start, length)
        } else if (inCoordinatesTag && inPointTag) {
            if (navigationDataSet.getCurrentPlacemark() == null) navigationDataSet.setCurrentPlacemark(
                Placemark()
            )
            buffer!!.append(ch, start, length)

//            navigationDataSet.getCurrentPlacemark()!!.coordinates = String(ch!!, start, length)
            navigationDataSet.getCurrentPlacemark()!!.coordinates =
                buffer.toString().trim { it <= ' ' }
        }
    }
}