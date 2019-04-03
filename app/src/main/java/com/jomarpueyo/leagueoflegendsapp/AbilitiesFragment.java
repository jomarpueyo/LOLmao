package com.jomarpueyo.leagueoflegendsapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.core.staticdata.ChampionSpell;
import com.merakianalytics.orianna.types.core.staticdata.SpellVariables;

import java.util.ArrayList;
import java.util.List;

public class AbilitiesFragment extends Fragment {
    private static final String TAG = "AbilitiesFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.abilities_fragment, container, false);

        ChampionDetailsTabs activity = (ChampionDetailsTabs) getActivity();
        Champion champ = activity.champData();

        //Change text on toggle switch
        Switch toggleAbilities;
        toggleAbilities = view.findViewById(R.id.toggleAbilities);
        //TODO: Temporarily disabled
        toggleAbilities.setVisibility(View.GONE);

        //Populate RecyclerView
        ArrayList<AbilitiesCard> abilitiesList = new ArrayList<>();
        loadCards(abilitiesList, champ, false);

        //Load default
        RecyclerView.LayoutManager mLayoutManager;
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(view.getContext());
        mAdapter = new AbilitiesAdapter(abilitiesList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //On Toggle Change
        //TODO: Temporarily disabled
        toggleAbilities.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    abilitiesList.clear();
                    loadCards(abilitiesList, champ, true);
                    toggleAbilities.setText(R.string.ToggleOnButton);
                }
                if (!b) {
                    abilitiesList.clear();
                    loadCards(abilitiesList, champ, false);
                    toggleAbilities.setText(R.string.ToggleOffButton);
                }

                //Update Recycler
                mAdapter = new AbilitiesAdapter(abilitiesList);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        return view;
    }

    private void loadCards(ArrayList<AbilitiesCard> abilitiesList, Champion champ, boolean detailsOn) {
        //TODO: Clean up this whole section
        //Spell Details and Ratios
        if (detailsOn) {
            //Passive
            abilitiesList.add(new AbilitiesCard(
                    champ.getPassive().getImage().getURL(),
                    champ.getPassive().getName(),
                    filterText(champ.getPassive().description())));

            //QWER Spells
            for (final ChampionSpell spell : champ.getSpells()) {

                String manaResource = spell.getResource();
                String resourceCost = "";
                String cooldowns = "";
                String seconds = "seconds";

                String damageToolTip = spell.getTooltip();

                //Replacing Spell Coefficients: {{ aX }}
                for (final SpellVariables var : spell.getVariables()) {
                    String spellCoef = "";
                    for (double coef : var.getCoefficients()) {
                        spellCoef = String.valueOf(coef);
                    }
                    damageToolTip = damageToolTip.replace("{{ " + var.getKey() + " }}", spellCoef);
                }

                //Replacing Effect Coefficients: {{ eX }}
                int j = 1;
                while (j < spell.getEffects().size()) {
                    String effectHolder = "{{ e" + j + " }}";
                    String effectString = spell.getEffects().get(j).toString();

                    List<Double> list = spell.getEffects().get(j);
                    if (list.get(0).equals(list.get(list.size() - 1))) {
                        effectString = list.get(0).toString();
                    }

                    damageToolTip = damageToolTip.replace(effectHolder, effectString.replaceAll("\\.0", ""));
                    j++;
                }

                //Replacing Ability Resources: {{ abilityResourceName }}
                if (manaResource.contains("{")) {
                    manaResource = champ.getResource();
                }

                //Organizing spell cost output
                int i = 0;
                for (int cost : spell.getCosts()) {

                    if (cost == 0 && champ.getResource().equals("No Cost")) break;

                    if (spell.getCosts().get(0).equals(spell.getCosts().get(2))) {
                        if (manaResource.equals("No Cost")) {
                            resourceCost = manaResource;
                            manaResource = "";
                            break;
                        } else {
                            resourceCost = cost + " " + manaResource + " at all levels";
                            manaResource = "";
                            break;
                        }
                    }

                    if (i == (spell.getCosts().size() - 1)) {
                        resourceCost += cost + " ";
                        break;
                    }
                    resourceCost += cost + ", ";
                    i++;
                }

                //Organizing Cooldown Output
                i = 0;
                for (Double cooldownNum : spell.getCooldowns()) {
                    String cooldown = cooldownNum.toString();
                    cooldown = cooldown.replaceAll("\\.0", "");

                    if (spell.getCooldowns().get(0).equals(spell.getCooldowns().get(2))) {
                        cooldowns = cooldown + " " + seconds + " at all levels";
                        seconds = "";
                        break;
                    }

                    if (i == (spell.getCosts().size() - 1)) { //Last iteration remove comma
                        cooldowns += cooldown + " ";
                        break;
                    }
                    cooldowns += cooldown + ", ";
                    i++;
                }

                //Add all texts to AbilitiesCard
                abilitiesList.add(new AbilitiesCard(
                        spell.getImage().getURL(),
                        spell.getName(),
                        filterText(damageToolTip)
                                + "\n" + resourceCost + manaResource
                                + "\n" + cooldowns + seconds
                ));
            }
        }
        //Spell Descriptions
        else {
            //passive
            abilitiesList.add(new AbilitiesCard(
                    champ.getPassive().getImage().getURL(),
                    champ.getPassive().getName(),
                    filterText(champ.getPassive().description())));

            //QWER Spells
            for (final ChampionSpell spell : champ.getSpells()) {
                abilitiesList.add(new AbilitiesCard(
                        spell.getImage().getURL(),
                        spell.getName(),
                        filterText(spell.getDescription())));

            }
        }
    }

    private String filterText(String s) {
        //Do not fix "Redundant"
        return s.replaceAll("<\\/{0,1}font.*?>", "")
                .replaceAll("<br>", "\n")
                .replaceAll("<[^>]*>", "");
    }

}
