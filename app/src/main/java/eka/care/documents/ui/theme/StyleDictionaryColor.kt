package eka.care.documents.ui.theme

import androidx.compose.ui.graphics.Color

object StyleDictionaryColor {
    // --- Primitives ---
    val colorNeutral100 = Color(0xffffffff)
    val colorNeutral200 = Color(0xfff5f5f5)
    val colorNeutral300 = Color(0xffededed)
    val colorNeutral400 = Color(0xffd1d1d1)
    val colorNeutral500 = Color(0xffa8a8a8)
    val colorNeutral600 = Color(0xff767676)
    val colorNeutral700 = Color(0xff595959)
    val colorNeutral800 = Color(0xff383838)
    val colorNeutral900 = Color(0xff1a1a1a)

    val colorPrimary100 = Color(0xffe4e1fa)
    val colorPrimary500 = Color(0xff6b5ce0)
    val colorPrimary700 = Color(0xff271b7e)

    val colorDanger100 = Color(0xffffeaeb)
    val colorDanger500 = Color(0xffd92d20)
    val colorDanger700 = Color(0xffb42318)

    val colorWarning100 = Color(0xfffffaeb)
    val colorWarning500 = Color(0xfff79009)
    val colorWarning700 = Color(0xffb54708)

    val colorSuccess100 = Color(0xffecfdf3)
    val colorSuccess500 = Color(0xff039855)
    val colorSuccess700 = Color(0xff027a48)

    val colorInfo100 = Color(0xfff0f9ff)
    val colorInfo500 = Color(0xff026aa2)
    val colorInfo700 = Color(0xff025582)

    // --- Scheme Colors ---
    val schemesShadow = Color(0xff000000)
    val schemesScrim = Color(0xff000000)
    val schemesPrimaryFixed = Color(0xffdce1ff)
    val schemesOnPrimaryFixed = Color(0xff04174b)
    val schemesPrimaryFixedDim = Color(0xffb6c4ff)
    val schemesOnPrimaryFixedVariant = Color(0xff354479)
    val schemesSecondaryFixed = Color(0xffdee1f9)
    val schemesOnSecondaryFixed = Color(0xff161b2c)
    val schemesSecondaryFixedDim = Color(0xffc2c5dd)
    val schemesOnSecondaryFixedVariant = Color(0xff424659)
    val schemesTertiaryFixed = Color(0xffffd7f5)
    val schemesOnTertiaryFixed = Color(0xff2c122a)
    val schemesTertiaryFixedDim = Color(0xffe3bada)
    val schemesOnTertiaryFixedVariant = Color(0xff5b3d57)

    // --- Additional Colors ---
    val colorsYellow = Color(0xffffcc00)
    val colorsMint = Color(0xff00c7be)
    val colorsTeal = Color(0xff38a9a9)
    val colorsCyan = Color(0xff32ade6)
    val colorsBrown = Color(0xff7f6545)
    val colorsIndigo = Color(0xff5856d6)
    val colorsPurple = Color(0xff9c3dd6)
    val colorsPink = Color(0xfff53da9)

    // --- Semantics ---
    val colorBackgroundSurface = colorNeutral100
    val colorBackgroundSubtle = colorNeutral200
    val colorBackgroundStrong = colorNeutral300
    val colorBackgroundPrimary = colorPrimary500
    val colorBackgroundPrimarySubtle = colorPrimary100
    val colorBackgroundDanger = colorDanger500
    val colorBackgroundDangerSubtle = colorDanger100
    val colorBackgroundWarning = colorWarning500
    val colorBackgroundWarningSubtle = colorWarning100
    val colorBackgroundSuccess = colorSuccess500
    val colorBackgroundSuccessSubtle = colorSuccess100
    val colorBackgroundInfo = colorInfo500
    val colorBackgroundInfoSubtle = colorInfo100

    val colorForegroundHeading = colorNeutral900
    val colorForegroundBody = colorNeutral700
    val colorForegroundSubtle = colorNeutral500
    val colorForegroundPlaceholder = colorNeutral500
    val colorForegroundOnPrimary = colorNeutral100
    val colorForegroundOnDanger = colorNeutral100
    val colorForegroundPrimary = colorPrimary500
    val colorForegroundDanger = colorDanger500
    val colorForegroundSuccess = colorSuccess500
    val colorForegroundWarning = colorWarning500
    val colorForegroundInfo = colorInfo500

    val colorBorderDefault = colorNeutral400
    val colorBorderSubtle = colorNeutral300
    val colorBorderPrimary = colorPrimary500
    val colorBorderDanger = colorDanger500
    val colorBorderFocus = colorPrimary500

    val colorLinkDefault = colorPrimary700
    val colorLinkHover = colorPrimary500
    val colorLinkPressed = colorPrimary700
    val colorLinkDisabled = colorNeutral500

    val colorFocusOutline = colorPrimary700
    val colorFocusBorder = colorPrimary500

    // --- Component Specific Colors ---
    val buttonPrimaryBackgroundColor = colorBackgroundPrimary
    val buttonPrimaryTextColor = colorForegroundOnPrimary
    val buttonSecondaryBackgroundColor = colorBackgroundSurface
    val buttonSecondaryTextColor = colorForegroundPrimary
    val buttonSecondaryBorderColor = colorForegroundOnPrimary
    val buttonDangerBackgroundColor = colorBackgroundDanger
    val buttonDangerTextColor = colorForegroundOnDanger

    val cardBackgroundColor = colorBackgroundSurface
    val cardBorderColor = colorBorderSubtle

    val alertInfoBackgroundColor = colorBackgroundInfoSubtle
    val alertSuccessBackgroundColor = colorBackgroundSuccessSubtle
    val alertWarningBackgroundColor = colorBackgroundWarningSubtle
    val alertDangerBackgroundColor = colorBackgroundDangerSubtle

    val formInputBackgroundColor = colorBackgroundSurface
    val formInputBorderColor = colorBorderDefault
    val formInputTextColor = colorForegroundBody
    val formInputPlaceholderColor = colorForegroundPlaceholder
    val formInputFocusBorderColor = colorBorderFocus
    val formLabelTextColor = colorForegroundHeading
}