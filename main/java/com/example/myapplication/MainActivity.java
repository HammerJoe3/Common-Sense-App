package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<News> list;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        NewsFeed news = new NewsFeed();
        news = news.getArticles("[{\"Title\":\"Are Bluetooth Headphones Dangerous? What The Experts Think\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/are-wireless-headphones-dangerous\",\"Date\":\"2019-03-29\"},{\"Title\":\"Baby Cough Syrup Is Recalled Over Bacteria Fears\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/baby-cough-syrup-is-recalled-over-bacteria-fears\",\"Date\":\"2019-03-29\"},{\"Title\":\"How Major Tech Companies are Handling Anti-Vaccine Content\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/can-mds-fight-rise-of-anti-vaxx-content-on-social-media\",\"Date\":\"2019-03-29\"},{\"Title\":\"Doctors Want to See Sodas Taken Out of Vending Machines\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/doctors-say-get-rid-of-sodas-in-vending-machines\",\"Date\":\"2019-03-29\"},{\"Title\":\"Should You Throw Out Your Avocados? Here's What to Know\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/everything-you-need-to-know-about-the-avocado-recall\",\"Date\":\"2019-03-29\"},{\"Title\":\"Game of Thrones' Emilia Clarke on Surviving Aneurysms\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/game-of-thrones-emilia-clarke-talks-surviving-aneurysms\",\"Date\":\"2019-03-29\"},{\"Title\":\"Here\\u0092s How Stress Can Trigger a Hormonal Imbalance\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/hormone-imbalances-and-how-to-treat-them\",\"Date\":\"2019-03-29\"},{\"Title\":\"It's Not Just Soda \\u0096 All Sweetened Drinks Can Raise Heart Disease Risk\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/its-more-than-soft-drinks-all-sweetened-drinks-can-raise-heart-disease-risk\",\"Date\":\"2019-03-29\"},{\"Title\":\"Lab-Grown 'Hearts' May Help Breast Cancer Patients\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/mds-create-mini-hearts-to-help-breast-cancer-patients\",\"Date\":\"2019-03-29\"},{\"Title\":\"Should Unvaccinated Kids Be Banned from Public Spaces?\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/measles-outbreak-worsens-ny-county-bans-unvaccinated-kids-from-public-spaces\",\"Date\":\"2019-03-29\"},{\"Title\":\"Yes, More People Are Getting Plastic Surgery. Here\\u0092s Why\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/more-people-getting-plastic-surgery\",\"Date\":\"2019-03-29\"},{\"Title\":\"New Study Shows \\u0091Female Viagra\\u0092 Is Safe to Take with Alcohol\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/new-study-show-female-viagra-drug-claim-medication-is-safe-to-take-with-alcohol-company-seeks-changes-to-fda-labeling\",\"Date\":\"2019-03-29\"},{\"Title\":\"Think Vaping with Your Kids in the Car Is Harmless? Think Again\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/parents-vaping-in-cars-with-kids\",\"Date\":\"2019-03-29\"},{\"Title\":\"What to Know About Psychosis Risk for ADHD Drugs Like Adderall\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/some-adhd-meds-may-increase-psychosis-risk\",\"Date\":\"2019-03-29\"},{\"Title\":\"Supplements May Lower Cholesterol \\u0097 But They Can Damage Your Liver\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/supplments-that-claim-to-lower-cholesterol-can-cause-liver-damage\",\"Date\":\"2019-03-29\"},{\"Title\":\"A Cream with Synthetic Vitamin D May Help Reduce Skin Cancer Risk\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/topical-immunotherapy-reduces-skin-cancer-risk\",\"Date\":\"2019-03-29\"},{\"Title\":\"This Common Diabetes Blood Test May Be Producing Inaccurate Results\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/what-blood-test-is-best-for-diagnosing-diabetes\",\"Date\":\"2019-03-29\"},{\"Title\":\"Kale Tops \\u0091Dirty Dozen\\u0092 Produce List\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/what-to-know-about-pesticide-dirty-dozen\",\"Date\":\"2019-03-29\"},{\"Title\":\"Why America\\u0092s Approach to HIV Prevention Has Stalled\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/why-hiv-prevention-has-stalled\",\"Date\":\"2019-03-29\"},{\"Title\":\"Why It\\u0092s a Terrible Idea to Expose Your Children to Chickenpox\",\"Link\":\"https:\\/\\/www.healthline.com\\/health-news\\/why-its-a-terrible-idea-to-deliberately-expose-your-children-to-chicken-pox\",\"Date\":\"2019-03-29\"},{\"Title\":\"Mosquitoes Can Deliver Virus Double Whammy\",\"Link\":\"https:\\/\\/www.webmd.com\\/a-to-z-guides\\/news\\/20161114\\/mosquitoes-can-deliver-zikachikungunya-double-whammy\",\"Date\":\"2019-03-29\"},{\"Title\":\"Can Some Kids Outgrow Autism?\",\"Link\":\"https:\\/\\/www.webmd.com\\/brain\\/autism\\/news\\/20190319\\/can-some-kids-outgrow-autism\",\"Date\":\"2019-03-29\"},{\"Title\":\"Vitamin D May Affect Breast Cancer Survival \",\"Link\":\"https:\\/\\/www.webmd.com\\/breast-cancer\\/news\\/20161110\\/vitamin-d-may-affect-breast-cancer-survival\",\"Date\":\"2019-03-29\"},{\"Title\":\"Lower Cholesterol From a Twice-a-Year Shot?\",\"Link\":\"https:\\/\\/www.webmd.com\\/cholesterol-management\\/news\\/20161115\\/coming-soon-lower-cholesterol-from-a-twice-a-year-shot\",\"Date\":\"2019-03-29\"},{\"Title\":\"Some Want a Federal Investigation as Insulin Prices Rise\",\"Link\":\"https:\\/\\/www.webmd.com\\/diabetes\\/news\\/20161108\\/insulin-price-hikes-draw-blood-criticism\",\"Date\":\"2019-03-29\"},{\"Title\":\"Research Sheds Light on Why People Who Lose Weight Gain It Back\",\"Link\":\"https:\\/\\/www.webmd.com\\/diet\\/news\\/20161014\\/how-your-appetite-can-sabotage-weight-loss\",\"Date\":\"2019-03-29\"},{\"Title\":\"Kale Is a Surprise on 2019's 'Dirty Dozen' List\",\"Link\":\"https:\\/\\/www.webmd.com\\/diet\\/news\\/20190320\\/kale-is-a-surprise-on-2019s-dirty-dozen-list\",\"Date\":\"2019-03-29\"},{\"Title\":\"Experts: Wheat Sensitivity Is Real\",\"Link\":\"https:\\/\\/www.webmd.com\\/digestive-disorders\\/celiac-disease\\/news\\/20161123\\/experts-wheat-sensitivity-is-real\",\"Date\":\"2019-03-29\"},{\"Title\":\"Bagged Salads May Be Fertile Ground for Bacteria \",\"Link\":\"https:\\/\\/www.webmd.com\\/food-recipes\\/food-poisoning\\/news\\/20161118\\/bagged-salads-may-be-fertile-ground-for-bacteria\",\"Date\":\"2019-03-29\"},{\"Title\":\"Water: Can It Be Too Much of a Good Thing? \",\"Link\":\"https:\\/\\/www.webmd.com\\/food-recipes\\/news\\/20161103\\/water-can-it-be-too-much-of-a-good-thing\",\"Date\":\"2019-03-29\"},{\"Title\":\"Buyer Beware: Seafood 'Fraud' Rampant, Report Says\",\"Link\":\"https:\\/\\/www.webmd.com\\/food-recipes\\/news\\/20190321\\/buyer-beware-seafood-fraud-rampant-report-says\",\"Date\":\"2019-03-29\"},{\"Title\":\"Trump Victory Offers Chance to Repeal Obamacare\",\"Link\":\"https:\\/\\/www.webmd.com\\/health-insurance\\/news\\/20161109\\/trump-wins-chance-to-dump-obamacare\",\"Date\":\"2019-03-29\"},{\"Title\":\"Your Apple Watch Might Help Spot Dangerous A-Fib\",\"Link\":\"https:\\/\\/www.webmd.com\\/heart-disease\\/atrial-fibrillation\\/news\\/20190318\\/your-apple-watch-might-help-spot-dangerous-a-fib\",\"Date\":\"2019-03-29\"},{\"Title\":\"Colorado Town Votes to Keep Pot Legal\",\"Link\":\"https:\\/\\/www.webmd.com\\/mental-health\\/addiction\\/news\\/20161031\\/why-one-colorado-town-may-push-back-on-legal-pot\",\"Date\":\"2019-03-29\"},{\"Title\":\"CBD Products Now Sold at CVS Stores in 8 States\",\"Link\":\"https:\\/\\/www.webmd.com\\/pain-management\\/news\\/20190322\\/cbd-products-now-sold-at-cvs-stores-in-8-states\",\"Date\":\"2019-03-29\"},{\"Title\":\"Acne Gives Up Secret That Points to New Treatments\",\"Link\":\"https:\\/\\/www.webmd.com\\/skin-problems-and-treatments\\/acne\\/news\\/20161028\\/acne-yields-up-secret-that-points-to-new-treatments\",\"Date\":\"2019-03-29\"}]");
         list = news.getNewsList();
        final ArrayList<String> arrayList = new ArrayList<>();


        for(int i =0; i< list.size(); i++) {
            arrayList.add(list.get(i).getArticleName());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPostion = position;
                String value = (String) listView.getItemAtPosition(position);

                int count;
                for(count = 0; count< arrayList.size(); count++){
                    if(value.equals(list.get(count).getArticleName())){
                        Intent i = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(list.get(count).getLink()));
                        startActivity(i);
                    }
                }
            }
        });
    }
}
