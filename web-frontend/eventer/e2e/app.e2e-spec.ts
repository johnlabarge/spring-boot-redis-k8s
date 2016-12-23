import { EventerPage } from './app.po';

describe('eventer App', function() {
  let page: EventerPage;

  beforeEach(() => {
    page = new EventerPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
