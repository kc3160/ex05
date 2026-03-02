import { Component, ElementRef, EventEmitter, Input, Output } from '@angular/core';
import { UserService } from '../user.service';
import { TagService } from '../tag.service';
import { Tag } from '../tag';
import { OnInit } from '@angular/core';

@Component({
  selector: 'app-tag-dropdown',
  standalone: false,
  templateUrl: './tag-dropdown.component.html',
  styleUrl: './tag-dropdown.component.css'
})
export class TagDropdownComponent implements OnInit {
  @Input() selected!: number;
  @Output() changed = new EventEmitter<{last: number, new: number}>();
  tags: Tag[] = [];
  private lastSelected = this.selected;

  constructor(private tagService: TagService, private host: ElementRef<HTMLSelectElement>) { }
  
  ngOnInit() {
    console.log(this.selected);
    if (this.selected == undefined) {
      this.selected = -1;
    }
    this.getTags();
  }

  getTags(): void {
    this.tagService.getTags().subscribe(n => this.tags = n);
  }

  changeTag(target: EventTarget|null): void {
    if (target == null) {
      return;
    }
    let newSelected: number = parseInt((target as HTMLSelectElement).value);
    if (Number.isNaN(newSelected) || newSelected == null) {
      newSelected = -1;
    }
    this.lastSelected = this.selected;
    this.selected = newSelected;
    this.changed.emit({last: this.lastSelected, new: this.selected});
    // Possibly leaking memory
    if (this.selected == -1) {
      this.host.nativeElement.remove();
    }
  }
}
