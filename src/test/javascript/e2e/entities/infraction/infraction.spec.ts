import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { InfractionComponentsPage, InfractionDeleteDialog, InfractionUpdatePage } from './infraction.page-object';

const expect = chai.expect;

describe('Infraction e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let infractionComponentsPage: InfractionComponentsPage;
  let infractionUpdatePage: InfractionUpdatePage;
  let infractionDeleteDialog: InfractionDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Infractions', async () => {
    await navBarPage.goToEntity('infraction');
    infractionComponentsPage = new InfractionComponentsPage();
    await browser.wait(ec.visibilityOf(infractionComponentsPage.title), 5000);
    expect(await infractionComponentsPage.getTitle()).to.eq('realEstateManagementServiceApp.infraction.home.title');
    await browser.wait(ec.or(ec.visibilityOf(infractionComponentsPage.entities), ec.visibilityOf(infractionComponentsPage.noResult)), 1000);
  });

  it('should load create Infraction page', async () => {
    await infractionComponentsPage.clickOnCreateButton();
    infractionUpdatePage = new InfractionUpdatePage();
    expect(await infractionUpdatePage.getPageTitle()).to.eq('realEstateManagementServiceApp.infraction.home.createOrEditLabel');
    await infractionUpdatePage.cancel();
  });

  it('should create and save Infractions', async () => {
    const nbButtonsBeforeCreate = await infractionComponentsPage.countDeleteButtons();

    await infractionComponentsPage.clickOnCreateButton();

    await promise.all([
      infractionUpdatePage.setDateOccurredInput('2000-12-31'),
      infractionUpdatePage.setCauseInput('cause'),
      infractionUpdatePage.setResolutionInput('resolution'),
      infractionUpdatePage.leaseSelectLastOption()
    ]);

    expect(await infractionUpdatePage.getDateOccurredInput()).to.eq('2000-12-31', 'Expected dateOccurred value to be equals to 2000-12-31');
    expect(await infractionUpdatePage.getCauseInput()).to.eq('cause', 'Expected Cause value to be equals to cause');
    expect(await infractionUpdatePage.getResolutionInput()).to.eq('resolution', 'Expected Resolution value to be equals to resolution');

    await infractionUpdatePage.save();
    expect(await infractionUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await infractionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Infraction', async () => {
    const nbButtonsBeforeDelete = await infractionComponentsPage.countDeleteButtons();
    await infractionComponentsPage.clickOnLastDeleteButton();

    infractionDeleteDialog = new InfractionDeleteDialog();
    expect(await infractionDeleteDialog.getDialogTitle()).to.eq('realEstateManagementServiceApp.infraction.delete.question');
    await infractionDeleteDialog.clickOnConfirmButton();

    expect(await infractionComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
