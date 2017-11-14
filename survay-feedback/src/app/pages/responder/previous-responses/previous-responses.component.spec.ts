import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PreviousResponsesComponent } from './previous-responses.component';

describe('PreviousResponsesComponent', () => {
  let component: PreviousResponsesComponent;
  let fixture: ComponentFixture<PreviousResponsesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PreviousResponsesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PreviousResponsesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
