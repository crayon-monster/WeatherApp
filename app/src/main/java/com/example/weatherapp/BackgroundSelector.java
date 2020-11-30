package com.example.weatherapp;

public class BackgroundSelector {
    public static int getBgInt(final int weatherConditionId) {
        switch (weatherConditionId) {
            case 200:
            case 201:
            case 202:
            case 210:
            case 211:
            case 212:
            case 221:
            case 230:
            case 231:
            case 232:
            case 300:
            case 301:
            case 302:
            case 310:
            case 311:
            case 312:
            case 313:
            case 314:
            case 321:
            case 500:
            case 501:
            case 502:
            case 503:
            case 504:
            case 511:
            case 520:
            case 521:
            case 522:
            case 531:
            case 781:
            case 701:
                return R.drawable.rain_bg;
            case 600:
            case 601:
            case 602:
            case 611:
            case 612:
            case 613:
            case 615:
            case 616:
            case 620:
            case 621:
            case 622:
            case 801:
            case 802:
            case 803:
            case 804:
            case 771:
            case 741:
            case 731:
            case 761:
            case 762:
            case 711:
                return R.drawable.cloud_bg;
            default:
                return R.drawable.sunny_bg;
        }
    }
}
