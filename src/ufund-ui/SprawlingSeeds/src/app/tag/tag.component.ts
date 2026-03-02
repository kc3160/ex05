import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Tag } from '../tag';
import { TagService } from '../tag.service';
import { FormsModule } from '@angular/forms';
import { UserService } from '../user.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-tag',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
  ],
  templateUrl: './tag.component.html',
  styleUrl: './tag.component.css'
})
export class TagComponent {
  @Input() tag!: Tag;
  @Output() deleteTag = new EventEmitter<number>();

  constructor(private tagService: TagService, private userService: UserService) { }

  update(): void {
    this.tagService.updateTag(this.tag).subscribe();
  }

  delete(): void {
    this.tagService.deleteTag(this.tag).subscribe();
    this.deleteTag!.emit(this.tag.id);
  }

  save(): void {
    this.tagService.updateTag(this.tag).subscribe();
  }

  isAdmin(): boolean {
    return this.userService.isLoggedIn() && this.userService.isAdmin;
  }
}
