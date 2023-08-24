/*
 * Copyright (C) 2023 imfai
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package prototype.lootengine;

import prototype.gameobjects.things.LootDrop;
import prototype.lootengine.WeaponPart;

/**
 *
 * @author imfai
 */
public interface LootEngine {
    

    
    public static WeaponPart getRandomWeaponPart(int level){
            int randomType = (int) ((Math.random() * 5) + 1);
            int randomPrefix = (int) ((Math.random() * 6) + 1);
            double randomRarity = Math.random();
            LootDrop.partType type;
            LootDrop.partPrefix prefix;
            LootDrop.partRarity rarity;
            switch (randomType) {
                case 2 ->
                    type = LootDrop.partType.BARREL;
                case 3 ->
                    type = LootDrop.partType.STOCK;
                case 4 ->
                    type = LootDrop.partType.GRIP;
                case 5 ->
                    type = LootDrop.partType.MAG;
                default->
                    type = LootDrop.partType.CORE;
            }
            switch (randomPrefix) {
                case 2 ->
                    prefix = LootDrop.partPrefix.HUGE;
                case 3 ->
                    prefix = LootDrop.partPrefix.MARKSMAN;
                case 4 ->
                    prefix = LootDrop.partPrefix.RAPID;
                case 5 ->
                    prefix = LootDrop.partPrefix.SPLATTER;
                case 6 ->
                    prefix = LootDrop.partPrefix.ODD;
                case 7 ->
                    prefix = LootDrop.partPrefix.SWIFT;
                default-> 
                    prefix = LootDrop.partPrefix.BASIC;
            }
            if (randomRarity <= 0.4) {
                rarity = LootDrop.partRarity.COMMON;
            } else if (randomRarity <= 0.65) {
                rarity = LootDrop.partRarity.UNCOMMON;
            } else if (randomRarity <= 0.7) {
                rarity = LootDrop.partRarity.UNIQUE;
            } else if (randomRarity <= 0.85) {
                rarity = LootDrop.partRarity.RARE;
            } else if (randomRarity <= 0.95) {
                rarity = LootDrop.partRarity.EPIC;
            } else {
                rarity = LootDrop.partRarity.LEGENDARY;
            }
        return new WeaponPart(type, prefix, rarity, level);
    }
    
    public static WeaponPart getRandomEnemyWeaponPart(int level){
            int randomType = (int) ((Math.random() * 5) + 1);
            int randomPrefix = (int) ((Math.random() * 4) + 1);
            double randomRarity = Math.random();
            LootDrop.partType type;
            LootDrop.partPrefix prefix;
            LootDrop.partRarity rarity;
            switch (randomType) {
                case 2 ->
                    type = LootDrop.partType.BARREL;
                case 3 ->
                    type = LootDrop.partType.STOCK;
                case 4 ->
                    type = LootDrop.partType.GRIP;
                case 5 ->
                    type = LootDrop.partType.MAG;
                default->
                    type = LootDrop.partType.CORE;
            }
            switch (randomPrefix) {
                case 2 ->
                    prefix = LootDrop.partPrefix.HUGE;
                case 3 ->
                    prefix = LootDrop.partPrefix.RAPID;
                case 4 ->
                    prefix = LootDrop.partPrefix.SPLATTER;
                case 5 ->
                    prefix = LootDrop.partPrefix.ODD;
                default-> 
                    prefix = LootDrop.partPrefix.BASIC;
            }
            if (randomRarity <= 0.4) {
                rarity = LootDrop.partRarity.COMMON;
            } else if (randomRarity <= 0.65) {
                rarity = LootDrop.partRarity.UNCOMMON;
            } else if (randomRarity <= 0.85) {
                rarity = LootDrop.partRarity.RARE;
            } else {
                rarity = LootDrop.partRarity.EPIC;
            }
        return new WeaponPart(type, prefix, rarity, level);
    }
}
