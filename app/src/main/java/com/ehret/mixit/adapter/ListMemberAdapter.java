/*
 * Copyright 2015 Guillaume EHRET
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ehret.mixit.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ehret.mixit.R;
import com.ehret.mixit.domain.people.Member;
import com.ehret.mixit.utils.FileUtils;
import com.github.rjeschke.txtmark.Processor;

import java.util.List;

/**
 * Adapter permettant l'affichage des données dans la liste des membres
 */
public class ListMemberAdapter extends BaseAdapter {

    private List<Member> datas;
    private Context context;

    public ListMemberAdapter(Context context, List<Member> datas) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Member getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_person, parent, false);
            holder = new ViewHolder();
            holder.profile_image = (ImageView) convertView.findViewById(R.id.person_user_image);
            holder.userName = (TextView) convertView.findViewById(R.id.person_user_name);
            holder.descriptif = (TextView) convertView.findViewById(R.id.person_shortdesciptif);
            holder.level = (TextView) convertView.findViewById(R.id.person_level);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Member person = datas.get(position);
        holder.userName.setText(person.getCompleteName());
        if (person.getShortDescription() != null) {
            holder.descriptif.setText(Html.fromHtml(Processor.process(person.getShortDescription().trim())).toString().replaceAll("\n", ""));
        }

        Bitmap image = null;

        //Si on est un sponsor on affiche le logo
        if(person.getLogo()!=null && person.getLogo().length()>0){
            image = FileUtils.getImageLogo(context, person);
        }
        if (image == null) {
            //Recuperation de l'mage liee au profil
            image = FileUtils.getImageProfile(context, person);
            if (image == null) {
                holder.profile_image.setImageDrawable(context.getResources().getDrawable(R.drawable.person_image_empty));
            }
        }

        if(person.getExtension()!=null && person.getExtension().equals("svg")){
            holder.profile_image.setAdjustViewBounds(true);
            holder.profile_image.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            holder.profile_image.setImageDrawable(FileUtils.getImageSvg(context, person));
        }
        else if(image!=null){
            holder.profile_image.setImageBitmap(image);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView userName;
        TextView descriptif;
        TextView level;
        ImageView profile_image;

    }

}
