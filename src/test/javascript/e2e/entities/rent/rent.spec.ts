import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { RentComponentsPage, RentDeleteDialog, RentUpdatePage } from './rent.page-object';

const expect = chai.expect;

describe('Rent e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rentComponentsPage: RentComponentsPage;
  let rentUpdatePage: RentUpdatePage;
  let rentDeleteDialog: RentDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Rents', async () => {
    await navBarPage.goToEntity('rent');
    rentComponentsPage = new RentComponentsPage();
    await browser.wait(ec.visibilityOf(rentComponentsPage.title), 5000);
    expect(await rentComponentsPage.getTitle()).to.eq('realEstateManagementServiceApp.rent.home.title');
    await browser.wait(ec.or(ec.visibilityOf(rentComponentsPage.entities), ec.visibilityOf(rentComponentsPage.noResult)), 1000);
  });

  it('should load create Rent page', async () => {
    await rentComponentsPage.clickOnCreateButton();
    rentUpdatePage = new RentUpdatePage();
    expect(await rentUpdatePage.getPageTitle()).to.eq('realEstateManagementServiceApp.rent.home.createOrEditLabel');
    await rentUpdatePage.cancel();
  });

  it('should create and save Rents', async () => {
    const nbButtonsBeforeCreate = await rentComponentsPage.countDeleteButtons();

    await rentComponentsPage.clickOnCreateButton();

    await promise.all([
      rentUpdatePage.setDueDateInput('2000-12-31'),
      rentUpdatePage.setReceivedDateInput('2000-12-31'),
      rentUpdatePage.setAmountInput('5'),
      rentUpdatePage.leaseSelectLastOption()
    ]);

    expect(await rentUpdatePage.getDueDateInput()).to.eq('2000-12-31', 'Expected dueDate value to be equals to 2000-12-31');
    expect(await rentUpdatePage.getReceivedDateInput()).to.eq('2000-12-31', 'Expected receivedDate value to be equals to 2000-12-31');
    expect(await rentUpdatePage.getAmountInput()).to.eq('5', 'Expected amount value to be equals to 5');

    await rentUpdatePage.save();
    expect(await rentUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await rentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Rent', async () => {
    const nbButtonsBeforeDelete = await rentComponentsPage.countDeleteButtons();
    await rentComponentsPage.clickOnLastDeleteButton();

    rentDeleteDialog = new RentDeleteDialog();
    expect(await rentDeleteDialog.getDialogTitle()).to.eq('realEstateManagementServiceApp.rent.delete.question');
    await rentDeleteDialog.clickOnConfirmButton();

    expect(await rentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
