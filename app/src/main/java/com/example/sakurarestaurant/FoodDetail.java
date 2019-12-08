package com.example.sakurarestaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.sakurarestaurant.Database.Database;
import com.example.sakurarestaurant.Model.Food;
import com.example.sakurarestaurant.Model.Order;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {

    TextView food_name, food_price, food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId = "";
    Food currentFood;

    FirebaseDatabase database;
    DatabaseReference foods;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        // Init Firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        //init view
        numberButton = findViewById(R.id.number_button);
        btnCart = findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodId,
                        currentFood.getName(),
                       numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()
                ));
                Toast.makeText(FoodDetail.this,"Added to Cart",Toast.LENGTH_SHORT).show();
            }
        });


        food_name = findViewById(R.id.food_name);
        food_image = findViewById(R.id.img_food);
        food_description = findViewById(R.id.food_description);
        food_price = findViewById(R.id.food_price);


        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        // get food id from intent
        if (getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");
        if (!foodId.isEmpty()) {
            getDetailFood(foodId);
        }
    }


    private void getDetailFood(String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                //load images
                Picasso.with(getBaseContext()).load(currentFood .getImage())
                        .into(food_image);

                //load images's name title
                collapsingToolbarLayout.setTitle(currentFood .getName());
                food_price.setText(currentFood .getPrice());
                food_description.setText(currentFood .getDescription());
                food_name.setText(currentFood .getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
