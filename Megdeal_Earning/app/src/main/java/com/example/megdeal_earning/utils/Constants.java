package com.example.megdeal_earning.utils;

public class Constants {
    //API URLs - Change this to your server URL
    public static final String BASE_URL = "http://192.168.1.35/";
    public static final String REGISTER_URL = BASE_URL + "register.php";
    public static final String LOGIN_URL = BASE_URL + "login.php";
    public static final String GET_BALANCE_URL = BASE_URL + "get_balance.php";
    public static final String UPDATE_BALANCE_URL = BASE_URL + "update_balance.php";

    public static final String GET_TRANSACTION_URL = BASE_URL + "get_transaction.php";
    public static final String GENERATE_CHECKSUM_URL = BASE_URL + "generate_paytm_checksum.php";
    public static final String PAYOUT_CALLBACK_URL = BASE_URL + "payout_Callback.php";

    //Shared Preference Keys
    public static final String PREF_NAME = "MegdealEarningAppPref";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_MOBILE = "user_mobile";
    public static final String KEY_USER_PAYTM = "user_paytm";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";

    // Paytm Credentials (Get from Paytm Dashboard)
    public static final String PAYTM_MID = "YOUR_MERCHANT_ID";
    public static final String PAYTM_MKEY = "YOUR_MERCHANT_KEY";
    public static final String PAYTM_WEBSITE = "DEFAULT";
    public static final String PAYTM_INDUSTRY_TYPE = "Retail";
    public static final String PAYTM_CHANNEL_ID = "WAP";

    // Minimum Withdrawal Amount
    public static final double MIN_WITHDRAWAL = 10.0;
    public static final double REFERRAL_BONUS = 50.0;
}
