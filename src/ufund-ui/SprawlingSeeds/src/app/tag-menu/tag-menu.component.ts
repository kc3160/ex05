import { Component, Input } from '@angular/core';
import { UserService } from '../user.service';
import { TagService } from '../tag.service';
import { Tag } from '../tag';

@Component({
  selector: 'app-tag-menu',
  standalone: false,
  templateUrl: './tag-menu.component.html',
  styleUrl: './tag-menu.component.css'
})
export class TagMenuComponent {
  @Input() compact!: boolean;
  newName: string = "";
  tags: Tag[];

  constructor(private userService: UserService, private tagService: TagService) { 
    this.tags = [];
    this.getTags();
  }

  getTags(): void {
    this.tagService.getTags().subscribe(n => this.tags = n);
  }

  addTag(name: string): void {
    if (name == "") {
      return;
    }
    this.tagService.createTag(new Tag(name, 0)).subscribe(
      t => { if (t != undefined) { this.tags.push(t) } }
    );
  }

  trackTag(index: number, tag: Tag): number {
    return tag.id;
  }

  deleteTag(id: number): void {
    this.tags = this.tags.filter(t => t.id != id);
  }
}
