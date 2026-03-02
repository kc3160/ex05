import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { NeedsService } from '../needs.service';
import { Need } from '../need';
import { Tag } from '../tag';
import { FormsModule, NumberValueAccessor } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { TagService } from '../tag.service';

@Component({
  selector: 'app-need-search',
  standalone: false,
  templateUrl: './need-search.component.html',
  styleUrl: './need-search.component.css'
})
export class NeedSearchComponent implements OnInit {
  needslist: Need[] = [];
  inputStr: string = "";
  tags: Tag[] = [];
  selectedTagIds: Set<number> = new Set<number>();
  lb:number = 0;
  ub:number = 9999999;
  constructor(
    private needService: NeedsService,
    private changeDetection: ChangeDetectorRef,
    private tagService: TagService
  ) {}

  ngOnInit(): void {
    this.getTags();
    this.searchNeeds();
  }

  getTags(): void {
    this.tagService.getTags().subscribe(tags => {
      this.tags = tags;
      this.changeDetection.detectChanges();
    });
  }

  onTagChange(tagId: number, target: EventTarget|null): void {
    if (target == null) {
      this.selectedTagIds.delete(tagId);
    }
    const checked = (<HTMLInputElement>target).checked;
    if (checked) {
      this.selectedTagIds.add(tagId);
    } else {
      this.selectedTagIds.delete(tagId);
    }
    console.log(this.selectedTagIds);
  }

  searchNeeds(): void {
    if (this.inputStr === undefined) {
      this.inputStr = "";
    }

    const selectedTagsStr = Array.from(this.selectedTagIds).join(',');
    console.log(`Searching for "${this.inputStr}" with tags: [${selectedTagsStr}]`);

    let tIds : number[] = Array.from(this.selectedTagIds);
    //placeholder costs
    this.needService.searchNeeds(this.inputStr,tIds,this.lb,this.ub).subscribe(needs => {
      this.needslist = needs;
      this.changeDetection.detectChanges();
    });
  }
}

