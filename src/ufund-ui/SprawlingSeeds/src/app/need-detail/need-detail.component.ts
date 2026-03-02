import { Component , OnInit, Input, ChangeDetectionStrategy } from '@angular/core';
import { NeedsService } from '../needs.service';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { BasketService } from '../basket.service';
import { Tag } from '../tag';
import { TagService } from '../tag.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef } from '@angular/core';

import { Need, ToolNeed, SeedNeed, FertilizerNeed, BundleNeed  } from '../need';
import { forkJoin, Observable, mergeMap } from 'rxjs';
import { map , concatMap} from 'rxjs/operators';


@Component({
  selector: 'app-need-detail',
  standalone: false,
  templateUrl: './need-detail.component.html',
  styleUrl: './need-detail.component.css',
  changeDetection: ChangeDetectionStrategy.Default
})
export class NeedDetailComponent implements OnInit {
  @Input() need!: Need;
  isRouted = false;
  @Input() inBun = false;
  toAdd = 1;
  tags: Tag[] = [];
  bunAddId = 0;
  bunAddQ = 0;
  bunAddNeed! : Need;
  bundleNeeds: Need[] = [];

  constructor(private route: ActivatedRoute,
              private needsService: NeedsService, 
              private location: Location,
              private basketService: BasketService,
              private tagService: TagService) {}


  ngOnInit() : void{
    if(!this.need) {
      const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
      this.needsService.getNeed(id).subscribe(n => {
        this.need = this.parseNeed(n);
        this.isRouted = true;
        if(this.need.type === 'bundle'){
          const needsMap = new Map<number, number>(Object.entries(this.asBun(this.need).needs).map(([k, v]) => [Number(k), v]));
          this.asBun(this.need).needs = needsMap;
          this.getBundleNeeds();
        }
        this.tagService.getNeedTags(this.need.id).subscribe(t => {
          t.push(new Tag("", -1)); 
          this.tags = t;
        });
    });
    }
    else if(this.need.type === 'bundle'){
      const needsMap = new Map<number, number>(Object.entries(this.asBun(this.need).needs).map(([k, v]) => [Number(k), v]));
      this.asBun(this.need).needs = needsMap;
      this.getBundleNeeds();
    }
  }

  isAdmin() : boolean {
    return this.basketService.isAdmin();
  }
  isValidUserForAdd() : boolean{
    return !this.basketService.isAdmin() && this.basketService.isLoggedIn(); 
  }

  getNeed(): void{
    const id = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.needsService.getNeed(id).subscribe(need => { 
      this.need = this.parseNeed(need); 
      this.tagService.getNeedTags(this.need.id).subscribe(tags => {
        tags.push(new Tag("", -1)); 
        this.tags = tags;
      });
    });
  }

  goBack(): void{
    this.location.back();
  }

  addNeedToBasket(): void{
    if (!this.need) return;
    const needAdd: Need = {...this.need, quantity: this.toAdd};
    this.basketService.addNeedToBasket(needAdd).subscribe();
  }

  incrementQuantity(n: number): void{
    let x = n + this.toAdd;
    if(x > this.need.quantity) x = this.need.quantity;
    this.toAdd = x;
  }
  decrementQuantity(n: number): void{
    let x = this.toAdd - n;
    if(x < 0) x = 0;
    this.toAdd = x;
  }
  parseNeed(need : Need): Need{
   if('organic' in need) return need as FertilizerNeed;
   else if ('used' in need) return need as ToolNeed;
   else if ('needs' in need) return need as BundleNeed;
   else return need;
  }

  asFert(need: Need): FertilizerNeed{
    return need as FertilizerNeed;
  }

  asTool(need: Need): ToolNeed{
    return need as ToolNeed;
  }


  //!TODO: have this also update the tags (admin changes) 
  save(): void{
    if(this.need){
      this.needsService.updateNeed(this.need).subscribe();
    }
  }

  changeTag(dropdown: {last: number, new: number}): void {
    this.tagService.updateNeedTag(this.need.id, dropdown.last, dropdown.new).subscribe(
      (_) => {
        this.tagService.getNeedTags(this.need.id).subscribe(tags => {
          tags.push(new Tag("", -1));
          this.tags = tags;
        });
      },
      (error: HttpErrorResponse) => {
        console.error(error);
      }
    );
  }

  tagTrack(index: number, tag: Tag): number {
    if (tag.id == -1) {
      // Force reloading
      return index; 
    }
    return tag.id;
  }

  asBun(need: Need): BundleNeed{
    return need as BundleNeed;
  }



  getBundleNeeds(): void {
    const needIds: number[] = Array.from(this.asBun(this.need).needs.keys());
    this.needsService.getNeedsByIds(needIds).subscribe(ns => {
      this.bundleNeeds = ns;
      this.bundleNeeds.map(n => n.quantity = this.asBun(this.need).needs.get(n.id) || 0 )
    });
  }

  addNeedToBundle() : void{
    this.needsService.getNeed(this.bunAddId).subscribe(n => {
      this.bunAddNeed = n;
      this.bunAddNeed.quantity = this.bunAddQ;
      this.asBun(this.need).needs.set(this.bunAddId,this.bunAddQ);
      this.needsService.addNeedToBundle(this.need.id, this.bunAddNeed).subscribe(_ =>{
        this.getBundleNeeds();
        this.bunAddQ = 0;
        this.bunAddId = 0;
      });
    } 
    );

  }
  removeFromBun(n : Need) : void{
    this.needsService.removeNeedFromBundle(this.need.id, n).subscribe(_ =>{
      this.asBun(this.need).needs.delete(n.id);
      this.getBundleNeeds();
    });

  }
  needById(index: number, need: Need): number{
    return need?.id;
  }

}

